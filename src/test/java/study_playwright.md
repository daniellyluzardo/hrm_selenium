# Playwright Review Guide — Understanding What You Already Use

**Who this is for:** you, someone who uses Playwright + TypeScript daily but wants to
understand it technically — not just operate it. Every section follows the same shape:
**what it is (plain words) → why it exists → proof in code → how to say it in an interview.**
No term is used before it's defined.

**Time budget:** ~half a day. Sections 1–4 are the core. Sections 5–6 are practice.

---

## 1. What Playwright actually is, underneath

When your test says `page.click(...)`, *something* has to carry that instruction into a
real browser. How that carrying works is the single biggest technical difference
between Playwright and Selenium — so let's build it up slowly.

### First, how Selenium does it (so we have a comparison)

Selenium works like sending letters through a post office:

```
Your Java code  →  HTTP request  →  driver program (chromedriver.exe)  →  browser
```

Every single command — every click, every findElement — is a **separate HTTP request**
(the same kind of request your browser makes to load a website). It goes to a separate
little program (the "driver") that translates it for the browser. Separate request,
per command, through a middleman. It works, but each command pays a round-trip cost,
and the middleman is one more thing that can be the wrong version or misbehave.

### How Playwright does it

Playwright works like a phone call instead of letters:

```
Your test code  ←──── one persistent open connection ────→  browser
```

When your test starts, Playwright opens **one connection** to the browser (a
*WebSocket* — think of it as a phone line that stays open, where both sides can talk
at any moment) and keeps it open for the whole test. Every command travels down that
already-open line instantly. For Chromium-family browsers this conversation happens in
a language called **CDP (Chrome DevTools Protocol)** — the exact same protocol the F12
DevTools panel uses to inspect a page. Playwright is essentially "DevTools, driven by
your code."

Two consequences you already benefit from without knowing it:

1. **No driver binary.** There's no chromedriver-equivalent to download or
   version-match. Playwright downloads its own browser builds (`npx playwright install`)
   and speaks to them directly. Every version-mismatch headache you've had with
   Selenium simply has no place to occur.
2. **The browser can talk back, instantly.** Because the line is open both ways,
   the browser can push events to Playwright the moment they happen ("that element
   just became visible"). Selenium can only find things out by *asking repeatedly*
   (polling). This is the mechanical root of why Playwright waits are smarter —
   next section builds on exactly this.

> **Interview one-liner:** "Selenium sends each command as a separate HTTP request
> through a driver binary; Playwright holds one persistent WebSocket connection
> straight to the browser. That removes the middleman and lets the browser push
> events back, which is what makes auto-waiting possible."

---

## 2. Browser → Context → Page (the three-layer hierarchy)

You destructure `{ page }` in every test and never think about where it comes from.
Here's the full chain it sits at the bottom of:

```
Browser          one running browser program (one Chromium process)
  └─ BrowserContext    one isolated "session" inside it
       └─ Page              one tab inside that session
```

**What a BrowserContext is, precisely:** an isolated profile inside one browser —
its own cookies, its own localStorage, its own logins. Like an incognito window:
same Chrome program, but what happens inside doesn't leak out. Creating a context
is nearly free (milliseconds); launching a whole browser is expensive (seconds).

**Why this matters — the parallelism math:**

- Selenium's isolation unit is the **whole browser**. 10 parallel tests = 10 full
  Chrome processes = heavy.
- Playwright's isolation unit is the **context**. 10 parallel tests = 1 Chrome
  process with 10 lightweight isolated sessions = cheap.

That's the honest technical answer to "why does Playwright parallelize so well" —
it's not magic speed, it's a cheaper isolation unit.

**Proof you can run** (this is what `@playwright/test` silently does for every test):

```ts
const browser = await chromium.launch();       // expensive — once
const context = await browser.newContext();    // cheap — per test
const page = await context.newPage();          // your familiar `page`
```

When a test finishes, its context is destroyed — cookies, logins, everything — which
is *why* your tests never contaminate each other, without you writing any cleanup.

---

## 3. Locator vs. WebElement — the deepest difference

This one deserves the slowest treatment, because it explains why an entire category
of Selenium bugs doesn't exist in your daily work.

### What a Selenium WebElement is

```java
WebElement button = driver.findElement(By.id("submit"));
```

The `findElement` call goes to the page **right now**, grabs the element that exists
**at this moment**, and hands you back a reference to *that specific DOM node* —
like taking a photograph of the button. If the page later re-renders (React replaces
the node, the page reloads), your photograph now points at a node that no longer
exists. Using it throws `StaleElementReferenceException` — "the thing in your photo
is gone."

### What a Playwright Locator is

```ts
const button = page.locator('#submit');
```

**Nothing is searched when this line runs.** No lookup, no photograph. A `Locator`
is a stored *recipe*: "when needed, find `#submit`." The search happens **fresh,
every single time you use it**:

```ts
await button.click();   // searches for #submit NOW, then clicks
await button.click();   // searches for #submit again NOW, then clicks
```

Even if React destroyed and rebuilt that button between the two clicks, the second
click finds the *new* node, because it never held onto the old one. There's no stale
photograph because there's no photograph — only the recipe.

> **Interview one-liner:** "A WebElement is a grabbed reference to one specific DOM
> node — it can go stale. A Locator is a stored query that re-executes on every use —
> so `StaleElementReferenceException` structurally cannot exist in Playwright."

This "recipe, not reference" idea is the same *lazy* principle as Selenium's
`PageFactory` `@FindBy` fields — except in Playwright it's not an optional pattern,
it's how the entire API works, everywhere, always.

---

## 4. Auto-waiting — what is actually checked

You know Playwright "waits automatically." Here's *what it actually does*, concretely.
Before performing `locator.click()`, Playwright repeatedly checks — using that open
phone line from Section 1 — until ALL of these are true (or the timeout expires):

1. the element is **attached** — it exists in the DOM at all
2. it is **visible** — not `display:none`, not zero-sized
3. it is **stable** — not mid-animation, hasn't moved between two frames
4. it is **enabled** — not `disabled`
5. it **receives events** — nothing (a loading overlay, a modal) is covering it

Only then does the click fire. Every action does the relevant subset of these checks.
That five-item list is roughly the `WebDriverWait` + `ExpectedConditions` code you
write by hand in Selenium — compressed into every action, invisibly.

### Web-first assertions — the same idea applied to asserting

```java
Assert.assertTrue(element.isDisplayed());     // Selenium/TestNG: checks ONCE, right now
```
```ts
await expect(locator).toBeVisible();          // Playwright: RETRIES until true or timeout
```

The TestNG assert is a snapshot — if the success message needs 300ms to appear, it
fails. The Playwright assert is a patient observer — it keeps re-checking. This is why
you almost never write "wait for X, then assert X" in Playwright: the assert *is* the
wait.

> **Interview trap this protects you from:** "How do you handle waits in Playwright?"
> Weak answer: "It's automatic." Strong answer: name the five conditions, then add:
> "and for anything auto-wait can't know about — like waiting for a specific API
> response — there's `page.waitForResponse()` and friends."

---

## 5. The user-facing locators — why getByRole beats CSS

You use `getByRole('button', { name: 'Submit' })` daily. The technical story:

Every page has a second, parallel representation you never see: the **accessibility
tree** — a simplified map of the page that screen readers use, where every element
has a **role** (button, link, heading, textbox...) and an **accessible name** (its
visible label). `getByRole` queries *that tree*, not the HTML.

Why that's more robust than CSS:

- CSS selectors describe **implementation**: "the element with class `btn-primary-lg`."
  A refactor renames the class → test breaks → but the user notices nothing wrong.
- Roles describe **what the user perceives**: "the button labeled Submit." The class
  can change, the tag can change from `<button>` to `<a>` styled as a button — as
  long as a user still sees a Submit button, the test still passes.

The test breaks only when the *user experience* breaks. That's the design goal, and
that's the sentence to say in an interview. (Bonus: it means your tests also quietly
verify the page is accessible enough to *have* proper roles and labels.)

Priority order Playwright itself recommends: `getByRole` → `getByLabel` /
`getByPlaceholder` / `getByText` → `getByTestId` → raw CSS/XPath as last resort.

---

## 6. Fixtures — the machinery behind `{ page }`

```ts
test('logs in', async ({ page }) => { ... });
```

That destructured `{ page }` is a **fixture**: a named, ready-made resource that the
test *declares it needs*, and the framework *builds before* the test and *tears down
after*. You never wrote `browser.newContext()` or `context.close()` — the fixture did
both, around your test.

Compare with what you built in Java in this project:

| Java/TestNG (you wrote it) | Playwright (framework provides it) |
|---|---|
| `BaseTest` with `@BeforeMethod` creating the driver | the built-in `page` fixture |
| `@AfterMethod` calling `driver.quit()` | automatic fixture teardown |

Same responsibility — setup/teardown around each test — but inverted: in TestNG *you
write the lifecycle*; in Playwright *you declare a dependency and the lifecycle comes
with it*. You can also define **custom fixtures** (e.g. a `loggedInPage` that arrives
already authenticated), which is the idiomatic Playwright replacement for base-class
inheritance.

One more lifecycle fact worth knowing: `storageState` lets you log in **once**, save
the session (cookies + localStorage) to a file, and have every context start already
logged in — the standard Playwright answer to "how do you avoid logging in before
every test."

---

## 7. The comparison table — with the *why* behind each row

This is the highest-probability interview question if your resume shows both tools.
Don't memorize the table — understand each row via the section that explained it.

| | Selenium | Playwright | Explained in |
|---|---|---|---|
| Transport | Per-command HTTP through a driver binary | One persistent WebSocket, no driver | §1 |
| Waiting | You write explicit/implicit waits | 5-condition auto-wait in every action | §4 |
| Stale elements | Possible — references to dead nodes | Impossible — locators are re-run recipes | §3 |
| Parallel isolation unit | Whole browser process (heavy) | BrowserContext (near-free) | §2 |
| Assertions | Evaluate once (TestNG/JUnit) | Retry until pass or timeout | §4 |
| Locator philosophy | CSS/XPath (implementation-facing) | Roles/labels (user-facing) first | §5 |
| Lifecycle | You build it (@BeforeMethod etc.) | Declared via fixtures | §6 |
| Browsers | Anything with a WebDriver implementation | Chromium, Firefox, WebKit (bundled) | — |
| Maturity | ~20 yrs, massive enterprise install base | 2020, Microsoft-backed, fast growth | — |
| Built-in tooling | Bring your own | Trace viewer, codegen, UI mode, HTML report | — |

**The balanced closing line** (interviewers listen for whether you're a zealot):
"Playwright is faster to write and stable by default; Selenium's edge is maturity,
language breadth, and the enormous installed base — which is exactly why enterprises
like SAP still run huge Selenium Java suites. The right tool usually follows the
team's existing stack, not a benchmark."

Also worth one sentence: **Playwright for Java exists** — same concepts, Java syntax,
but no bundled test runner (you pair it with JUnit or TestNG). If a Java shop
modernized without changing language, that's the path.

---

## 8. Hands-on exercises (~90 min, in your normal TS setup)

Each targets a section above — the point is *seeing* the concept, not the output.

1. **See the recipe re-run (§3).** Make a locator for an element, `click()` it, force
   the page to re-render that element (navigate away and back, or trigger a re-render),
   `click()` again. No stale error. Now describe *why* in one sentence, out loud.
2. **See auto-wait blink (§4).** On a page with a loading overlay/spinner, click a
   button that's briefly covered. Watch Playwright wait out the overlay with zero wait
   code. Then set `timeout: 500` on that click and watch condition #5 fail with a
   precise error naming what blocked it.
3. **See contexts isolate (§2).** In one test, create two contexts, log in inside
   context A only, open the same page in context B — B is logged out. One browser,
   two sealed worlds.
4. **See the retry-assert (§4).** `await expect(locator).toBeVisible()` on something
   that appears after a delay — passes. Replace with a plain instant check — fails.
5. **Read one trace.** Run any failing test with `--trace on`, open the trace viewer,
   and find: the failing step, the DOM snapshot at that moment, and the network tab.
   Being able to *describe* this flow is a strong debugging-question answer.

---

## 9. Mock questions — with what a good answer contains

Say these out loud. The bullets are the ingredients, not a script.

**"Why are Playwright tests less flaky than Selenium's?"**
Auto-wait's five conditions before every action (§4) + locators re-resolving instead
of holding references (§3). Name both mechanisms; most candidates only say "it waits."

**"What is a BrowserContext and why does it matter?"**
Isolated session inside one browser process; near-free to create; the reason parallel
Playwright runs are cheap and tests can't contaminate each other (§2).

**"How does Playwright communicate with the browser?"**
Persistent WebSocket, CDP for Chromium, no driver binary; browser pushes events back
instead of being polled (§1).

**"Why prefer getByRole over CSS selectors?"**
Queries the accessibility tree — tests what the user perceives, not the
implementation; breaks only when the UX breaks (§5).

**"A test is flaky — walk me through debugging it in Playwright."**
Reproduce with `--trace on` → trace viewer: failing step, DOM snapshot at that instant,
network + console →  common culprits: a race auto-wait can't see (waiting for the wrong
signal), test interdependence, or an implementation-facing locator (§5, §8.5).

**"When would you still choose Selenium?"**
Existing large suite and team skills, broader language needs, exotic/legacy browser
requirements, ecosystem mandates. Never answer "never" — tool zealotry reads junior.

---

## 10. Suggested half-day schedule

| Block | Time | Do |
|---|---|---|
| 1 | 60 min | Read §1–§6 slowly; after each section, close the doc and re-explain it out loud in your own words. If you can't, re-read — that's the signal. |
| 2 | 90 min | Exercises §8, in order. |
| 3 | 30 min | Table §7 — reconstruct it from memory on paper, checking against the doc. |
| 4 | 45 min | Mock questions §9, answered out loud, standing up, no peeking. |
