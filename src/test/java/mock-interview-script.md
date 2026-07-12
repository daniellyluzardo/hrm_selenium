# Mock Interview Script — SAP Quality Engineer Prep
### A guide for the interviewer (no technical background needed)

## How to run this

- **Total time:** 60–90 minutes if you use everything. Sections are independent —
  you can skip or reorder. If short on time, prioritize Sections 2, 3, 6, and 9.
- **Your job is NOT to understand the answers technically.** Under each question
  there's a **"Listen for"** box: specific words, ideas, and analogies a strong
  answer contains. You're pattern-matching against that list, not evaluating logic.
- **Push back once per section.** After any answer, pick one of these and say it —
  this simulates real interview pressure:
  - "Explain that again like I'm not technical at all."
  - "Why does that matter? So what?"
  - "Are you sure?" (even when they're right — composure is being tested)
- **Silence rule:** if they go quiet for more than ~15 seconds, note it. The single
  most important habit being trained is *thinking out loud*. Long silence in the
  real interview is a fail-signal even when the final answer is right.
- **Scoring per question**, jot in the margin:
  - ✅ hit the listen-for points, spoke fluidly
  - 🟡 got there but slowly, or missed the "why it matters" part
  - ❌ blanked, rambled, or went silent
  At the end, every 🟡/❌ is a topic to review tonight.

---

## Section 1 — Warm-up (5 min, don't skip — it's rapport AND real practice)

**Q1.1 — "Tell me about yourself and your experience with test automation."**

> **Listen for:** a STRUCTURED answer under 2 minutes: current role → main tools
> (should mention Playwright with JavaScript at work, and recently building a
> Selenium Java framework) → why this SAP role. ❌ if it rambles past 3 minutes or
> is just a resume recital with no thread.

**Q1.2 — "Rate yourself in Java from 1 to 10, and defend the number."**

> **Listen for:** a specific number (not "it depends"), followed immediately by
> evidence — e.g. "a 6: I'm comfortable with OOP concepts and built a test framework
> with them, but I haven't done heavy multithreading." Honesty + evidence = ✅.
> A bare number with no defense = 🟡.

---

## Section 2 — Java & Object-Oriented Programming (15 min)

**Q2.1 — "What are the four pillars of OOP? Give me a real example of each from
your own test framework."**

> **Listen for:** all four names — **encapsulation, inheritance, polymorphism,
> abstraction** — each with a testing example, not a textbook one. Good signs:
> "my page objects hide their locators as private fields" (encapsulation);
> "my LoginPage extends BasePage so it inherits the driver setup" (inheritance);
> "the same click() call behaves differently in Chrome vs Firefox" (polymorphism).
> ❌ if they can only list names without examples.

**Q2.2 — "What's the difference between an interface and an abstract class?"**

> **Listen for:** the checklist-vs-partial-blueprint idea: an interface is a *list of
> promises with no actual code*; an abstract class *has some real working code plus
> some deliberate blanks*. Bonus points if they mention: a class can implement MANY
> interfaces but extend only ONE class, and that Selenium's WebDriver is itself an
> interface while ChromeDriver is a class implementing it. That Selenium connection
> is the ✅ marker.

**Q2.3 — "To use an abstract class, do you write `extends` or `implements`? Why?"**

> **Listen for:** "**extends**" — immediately, without hesitation — and the reason:
> an abstract class is still a CLASS; `implements` is reserved only for interfaces.
> This was drilled recently; hesitation here = 🟡, review tonight.

**Q2.4 — "What do private, protected, and public mean? And what happens if you
write none of them?"**

> **Listen for:** private = only that class itself; protected = the class plus its
> subclasses; public = anyone. The trap is the last part: writing NOTHING is a real
> fourth level called **package-private / default** — usable only within the same
> package. If they nail the fourth one unprompted: strong ✅.

**Q2.5 — "What is a static variable? Why did your framework use one for the
WebDriver?"**

> **Listen for:** static = belongs to the CLASS itself, one shared copy, rather than
> each object having its own. The framework connection: their Cucumber Hooks class
> holds a static driver so step-definition classes can reach the same browser
> instance. Bonus: mentioning this becomes a problem in PARALLEL runs (tests fighting
> over one driver) and naming ThreadLocal as the fix.

**Q2.6 — "What's the difference between == and .equals() for Strings in Java?"**

> **Listen for:** `==` compares whether two references point at the SAME object;
> `.equals()` compares the CONTENT. For text comparison you almost always want
> `.equals()`. Short and confident = ✅.

---

## Section 3 — Selenium (20 min — the heart of the interview)

**Q3.1 — "Explain Selenium's architecture. What actually happens when your code
says driver.click()?"**

> **Listen for:** the chain: their Java code → Selenium's client library → an HTTP
> request in the **W3C WebDriver protocol** → a **browser driver** (like
> chromedriver) → the real browser. Bonus: Selenium 4 removed the old translation
> layer (JSON Wire Protocol) by standardizing on W3C. A drawn-out "it's magic"
> answer = ❌.

**Q3.2 — "What kinds of waits exist in Selenium, and which do you use?"**

> **Listen for:** three types — **implicit** (a global default applied to every
> element search), **explicit** (waiting for one specific condition, like an element
> becoming clickable), **fluent** (explicit plus custom polling). The KEY point that
> must appear: **never mix implicit and explicit waits** — combined they cause
> unpredictable timing. Their stated preference should be explicit waits everywhere.
> Missing the "never mix" warning = 🟡.

**Q3.3 — "How do you decide which locator to use for an element?"**

> **Listen for:** a PRIORITY ORDER, not a list: id first (if stable), then
> test-specific attributes like data-testid, then name, then CSS selectors, with
> XPath as a later resort and **absolute XPath never**. Two bonus signals of real
> experience: (1) testing a locator in the browser DevTools console before using it,
> (2) checking that an id isn't auto-generated by refreshing the page and re-inspecting.

**Q3.4 — "Your test fails with StaleElementReferenceException. What happened and
how do you fix it?"**

> **Listen for:** the element reference they held became invalid because the page
> re-rendered or reloaded — the DOM node was destroyed and rebuilt. Fix: re-locate
> the element after whatever caused the change. Big bonus if they add: "this
> exception can't happen in Playwright, because its locators re-search every time" —
> that cross-tool insight is senior-level.

**Q3.5 — "How would you automate: a dropdown, a popup alert, and a form inside an
iframe?" (ask all three)**

> **Listen for:** dropdown → the **Select** class, but ONLY for real `<select>`
> HTML elements (custom styled dropdowns need plain clicks) — that distinction is
> the experience marker. Alert → `driver.switchTo().alert()` then accept/dismiss.
> Iframe → must **switch INTO the frame first** (`switchTo().frame(...)`), act,
> then `switchTo().defaultContent()` to escape. Missing the "must switch first"
> point on iframes = 🟡.

**Q3.6 — "What is the Page Object Model and why bother with it?"**

> **Listen for:** each page gets its own class holding that page's locators and
> actions; tests call methods and NEVER touch locators directly. The "why" that must
> appear: **when the UI changes, you fix ONE class instead of hunting through every
> test**. Bonus: they built exactly this — LoginPage, CandidatePage, a BasePage
> with shared setup — and can describe it.

**Q3.7 — "What does PageFactory.initElements do?"**

> **Listen for:** the empty-box-with-sticky-note idea (or any equivalent): @FindBy
> fields start EMPTY; initElements is the step that wires each one to a real element
> finder. Bonus word: **"lazily"** — the actual page search only happens the first
> time each field is USED, which gives pages time to load.

---

## Section 4 — TestNG & Framework Design (10 min)

**Q4.1 — "Walk me through the TestNG annotation execution order."**

> **Listen for:** the nesting: BeforeSuite → BeforeTest → BeforeClass →
> BeforeMethod → **Test** → AfterMethod → AfterClass → AfterTest → AfterSuite.
> They don't need it word-perfect, but Before/AfterMethod wrapping EVERY test
> (vs. Before/AfterClass running once per class) must be clear.

**Q4.2 — "When would you use @BeforeMethod vs @BeforeClass for browser setup?"**

> **Listen for:** @BeforeMethod (fresh browser per test) as the SAFE DEFAULT —
> full test independence, no leaked state. @BeforeClass reserved for genuinely
> SEQUENTIAL flows where steps share one session (they built a candidate-onboarding
> flow this way, using dependsOnMethods to enforce order). If they can articulate
> the trade-off — reliability vs. speed — that's ✅.

**Q4.3 — "What is a DataProvider and what shape does its data have?"**

> **Listen for:** it feeds one test method MULTIPLE sets of inputs — the test runs
> once per data row. The shape: **Object[][]**, a grid where each inner row's value
> count must EXACTLY match the test method's parameter count. Bonus if they recall
> that a mismatched row count breaks at runtime — they hit this bug personally.

**Q4.4 — "What's the difference between a hard assert and a soft assert?"**

> **Listen for:** hard assert (Assert.assertEquals etc.) stops the test dead at the
> first failure; **SoftAssert collects all failures** and reports them together when
> you call assertAll() — useful for checking several things on one page in one pass.

**Q4.5 — "You also set up Cucumber. What do the feature file, step definitions,
and runner each do?"**

> **Listen for:** feature file = plain-English scenarios in **Gherkin**
> (Given/When/Then) that non-technical people can read; step definitions = the Java
> methods glued to each English line; runner = the class that kicks it all off.
> The KEY insight: their **page objects needed ZERO changes** when adding Cucumber —
> proof the layers are properly separated. Bonus: Cucumber's @Before/@After hooks
> are DIFFERENT from TestNG's @BeforeMethod — a bug they personally hit and fixed.

---

## Section 5 — Playwright & Tool Comparison (10 min)

**Q5.1 — "You use Playwright daily. Compare it with Selenium — architecture,
waiting, and reliability."**

> **Listen for:** three contrasts: (1) **transport** — Selenium sends each command
> as a separate HTTP request through a driver program; Playwright keeps ONE
> persistent open connection straight to the browser, no middleman; (2) **waiting**
> — Playwright auto-waits before every action (element visible, stable, enabled,
> not covered) while Selenium waits are hand-written; (3) **staleness** — Playwright
> locators are re-run "recipes" so stale elements can't exist. A balanced closing —
> "Selenium's strength is maturity and enterprise install base, which is why SAP
> runs it" — is the ✅ marker. Tool zealotry ("Selenium is obsolete") = 🟡.

**Q5.2 — "What is a BrowserContext and why does it make Playwright fast in
parallel?"**

> **Listen for:** an isolated session INSIDE one browser — like an incognito window;
> its own cookies and logins; nearly free to create. Parallel math: Selenium
> isolates per whole browser (heavy), Playwright per context (cheap) — so 10
> parallel tests ≠ 10 Chromes.

**Q5.3 — "Why does Playwright recommend getByRole over CSS selectors?"**

> **Listen for:** roles query the **accessibility tree** — the page as a USER
> perceives it (a button labeled Submit) rather than as it's implemented (a CSS
> class name). Result: the test breaks only when the user experience breaks, not
> when a developer renames a style class.

**Q5.4 — "A Playwright test is flaky. Walk me through debugging it."**

> **Listen for:** run with **trace on** → open the **trace viewer** → inspect the
> failing step's DOM snapshot, network calls, console. Then typical culprits: a race
> the auto-wait can't see, tests depending on each other, or a fragile
> implementation-facing locator.

---

## Section 6 — SQL (10 min — recently studied, verify it stuck)

*For this section, tell them: "Imagine a Customers table and an Orders table, where
orders point at customers by customer id. Some customers have never ordered."*

**Q6.1 — "What's a primary key and a foreign key, and why do we need the foreign
key if the primary key already exists?"**

> **Listen for:** primary key = each row's unique ID within ITS OWN table (a CPF).
> Foreign key = a column POINTING at another table's primary key. The "why both":
> the primary key only protects uniqueness inside its own table — the foreign key is
> what stops garbage pointers (an order referencing customer 999 who doesn't exist).
> **Two keys, two different jobs** — that framing is the ✅.

**Q6.2 — "Explain INNER JOIN vs LEFT JOIN. Which customers disappear from an
INNER JOIN result, and why would anyone want them back?"**

> **Listen for:** INNER JOIN keeps ONLY rows that match on both sides — customers
> with zero orders VANISH. LEFT JOIN keeps EVERY row of the left table, filling the
> missing side with **NULL**. The "why want NULLs": sometimes the no-matches ARE the
> answer — "which customers never ordered" is only answerable via LEFT JOIN +
> `WHERE ... IS NULL`. If they volunteer that it's also a QA scenario (testing how
> the app handles zero-order customers): strong ✅.

**Q6.3 — "What is an index, and what's the trade-off?"**

> **Listen for:** the book-index analogy — jump straight to the row instead of
> reading every page. The trade-off MUST appear: reads on that column get faster,
> but every insert/update gets slightly slower because the index needs updating too.
> Answer without the trade-off = 🟡.

**Q6.4 — "What is a trigger? Give a use case."**

> **Listen for:** an automatic stored action fired when a table changes — the
> classic: on deleting an employee row, first copy it into an audit table
> automatically, so nobody has to remember to.

**Q6.5 — "Say out loud, roughly, the SQL for: total spend per customer, including
customers who spent nothing."**

> **Listen for (this is the hardest one):** three ingredients must all appear —
> **LEFT JOIN** (so zero-order customers survive), **SUM(amount)**, and
> **GROUP BY** the customer. Rough syntax is fine; the three ingredients are what
> count. All three = strong ✅. Only INNER JOIN or no GROUP BY = 🟡, review tonight.

---

## Section 7 — Puzzles (10 min — grade the NARRATION, not the answer)

*Rules for you: give NO hints for 2 minutes. What you're grading is whether they
think OUT LOUD. Silence is the failure mode, not wrongness.*

**Q7.1 — "25 machines produce 1kg weights. One broken machine produces 999g
weights. You have a precise scale you may use exactly ONCE. Find the broken
machine."**

> **Listen for:** the trick is taking a DIFFERENT count from each machine — 1 weight
> from machine 1, 2 from machine 2, ... 25 from machine 25 — then weighing the whole
> pile once. The grams MISSING from the perfect total literally spell out the broken
> machine's number (17g short = machine 17). ✅ if the "unequal amounts so each
> culprit leaves a unique fingerprint" idea appears. GOLD if they say "let me try a
> smaller version with 3 machines first" — that instinct is the real skill.

**Q7.2 — "10 identical balls, one heavier. A balance scale (two pans, no numbers).
Minimum weighings to guarantee finding it?"**

> **Listen for:** the key insight FIRST: one weighing has THREE outcomes (left
> sinks, right sinks, balance) — so split into thirds, not halves, with one group
> OFF the scale. Then: 9 balls resolve in 2 weighings (9→3→1), and the 10th ball
> overflows that, forcing **3 in the worst case**. The reasoning walked aloud
> matters far more than the number.

**Q7.3 (only if time) — invent any estimation question, e.g. "how many coffees does
a café in a busy neighborhood sell per day?"**

> **Listen for:** structured decomposition spoken aloud — opening hours × customers
> per hour × rush vs. quiet periods — with stated assumptions. ANY reasoned number
> is a pass; a bare guess with no shown reasoning is the only fail.

---

## Section 8 — API, Git, Manual Testing (8 min, rapid-fire)

**Q8.1 — "What is API testing and why do teams want MANY API tests but fewer UI
tests?"**

> **Listen for:** testing programs' direct communication — sending requests,
> checking responses, no browser. The "why": API tests are much faster and less
> flaky (no page loads, no locators), so the healthy shape is many API tests, UI
> automation reserved for real user journeys. Mentioning **Postman** (manual) and
> **REST Assured** (Java automation, plugs into TestNG like Selenium does) = ✅.

**Q8.2 — "A request returns 401. Another returns 500. Whose fault is each?"**

> **Listen for:** 401 = the CALLER's fault (not authenticated — 4xx family is
> "my fault"); 500 = the SERVER's fault (5xx family is "their fault"). Instant,
> confident = ✅.

**Q8.3 — "git pull vs git fetch — crisp answer."**

> **Listen for:** fetch downloads new commits WITHOUT touching your working code
> (look before you merge); pull = fetch + merge immediately. "Fetch is the cautious
> two-step, pull is the shortcut."

**Q8.4 — "Smoke vs sanity vs regression vs re-testing — quick definitions."**

> **Listen for:** smoke = quick check that a build's critical paths work at all;
> sanity = narrow check of one specific fix; regression = re-running existing tests
> to confirm nothing OLD broke; re-testing = re-running the exact test that found a
> specific bug to confirm THAT bug is fixed. The regression/re-testing distinction
> is the one people blur — listen closely there.

**Q8.5 — "Walk me through the defect life cycle."**

> **Listen for:** New → Assigned → Open → Fixed → Retest → Verified → Closed,
> with the loop-back: fails retest → **Reopened**. Naming the reopen path = ✅.

---

## Section 9 — Managerial & HR (10 min — YOU are strong here; be a real manager)

**Q9.1 — "Why SAP?"**

> **Listen for:** something SPECIFIC — their products, the scale of quality
> engineering there, the domain — not generic praise. Push back once: "that could
> describe any big company. Why SAP specifically?"

**Q9.2 — "Tell me about a task that wasn't finished on time and you had to make
quick decisions."**

> **Listen for:** ONE concrete, true story with shape: situation → the decision
> made → outcome → what they'd repeat or change. Vague generalities ("I always
> communicate early") without a story = 🟡.

**Q9.3 — "What if we offered you a tester position rather than developer?"**

> **Listen for (asked in a real SAP interview):** a decided, genuine stance — a
> strong one embraces the hybrid: "I write code, and my code's purpose is quality —
> that's what Quality Engineer means to me." Visible hesitation or contradiction =
> they haven't decided; make them decide tonight.

**Q9.4 — "What's a real weakness of yours?"**

> **Listen for:** a REAL one plus the active countermeasure. Auto-fail: "I'm a
> perfectionist" or any humble-brag.

**Q9.5 — End with: "Do you have any questions for me?"**

> **Listen for:** they must have TWO prepared. Good ones: how the QE team splits
> manual vs automation; what quality ownership looks like day to day. Having none
> is a real-interview fail; treat it as ❌ here too.

---

## After the interview — 10-minute debrief, together

1. Read back every 🟡 and ❌ with its section number — that's tonight's review list,
   already prioritized.
2. Tell them the single moment they sounded MOST confident, and the single moment
   they sounded least. (They can't hear this from inside their own head.)
3. One honesty check: did they ever bluff instead of saying "I don't know, but
   here's how I'd reason about it"? Bluffing reads worse than honest uncertainty
   in real interviews — flag it if you heard it.
4. If they went silent on the puzzles: rerun ONE puzzle at the end, with the only
   rule being "narrate every thought, even bad ones." The habit, not the answer,
   is the training goal.
