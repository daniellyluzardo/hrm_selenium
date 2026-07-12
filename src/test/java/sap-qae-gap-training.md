# SAP QAE Gap Training — SQL, Puzzles, API, Git, HR

**How to read this:** top to bottom, no skipping inside Section 1 — every SQL idea
builds on the one before it. Sections 3–5 are short and standalone. Nothing here
requires a computer; every example shows you its data and its result right on the page.

---

# Section 1 — SQL, from actual zero

## 1.1 What a database table even is

A table is a spreadsheet with rules. Columns are defined once (name, type); every row
must follow them. That's the whole mental model — if you can read a spreadsheet, you
can read a table. We'll use these two tables for this ENTIRE section, so get familiar
with them now:

**Customers**

| customer_id | name    | city          |
|-------------|---------|---------------|
| 1           | Ana     | Porto Alegre  |
| 2           | Bruno   | São Paulo     |
| 3           | Carla   | Curitiba      |
| 4           | Diego   | Porto Alegre  |

**Orders**

| order_id | customer_id | amount |
|----------|-------------|--------|
| 101      | 1           | 250    |
| 102      | 1           | 100    |
| 103      | 3           | 80     |

Read the Orders table out loud once: "Order 101 was placed by customer 1 for 250.
Order 102 — also customer 1 — for 100. Order 103 — customer 3 — for 80."
Notice two things, because the whole section hangs on them:

- **Ana (customer 1) has TWO orders. Carla (3) has one. Bruno (2) and Diego (4)
  have NONE.**
- Orders doesn't repeat the customer's name or city — it only stores the customer's
  **number**. That number is the thread connecting the two tables.

## 1.2 Primary key — "each row's unique ID"

`customer_id` in Customers is its **primary key**: a column where every row's value is
unique and never empty. Like a CPF — it exists so that "customer 1" can only ever mean
one specific person. Same for `order_id` in Orders.

## 1.3 Foreign key — "a pointer to another table's primary key"

`customer_id` inside **Orders** is a **foreign key**: it points at Customers'
primary key. Its job is protecting the *relationship*: the database will refuse an
order with `customer_id = 99`, because no customer 99 exists to point at.

**The interview question this answers** ("why need a foreign key if we already have a
primary key?"): the primary key only guarantees uniqueness *inside its own table*.
It does nothing to stop Orders from containing garbage pointers. The foreign key is
the rule that makes cross-table references trustworthy. Two keys, two different jobs.

## 1.4 SELECT and WHERE — reading rows

```sql
SELECT name, city FROM Customers WHERE city = 'Porto Alegre';
```

Read it as a sentence: "give me the name and city columns, from the Customers table,
but only rows where city is Porto Alegre." Result:

| name  | city         |
|-------|--------------|
| Ana   | Porto Alegre |
| Diego | Porto Alegre |

`SELECT * FROM Customers` means "all columns." That's genuinely all a basic query is.

## 1.5 JOINs — the slow, thorough version

### The problem JOINs solve

Look at Orders again. It knows order 101 belongs to *customer number 1* — but not
that customer 1 is named Ana or lives in Porto Alegre. That information lives in the
OTHER table. Any question touching both — "show each order WITH the customer's name" —
requires stitching the two tables together. That stitching is a JOIN. Nothing more.

### How to think about what a JOIN physically does

For every row in the first table, the database walks over to the second table and asks:
**"do you have any rows whose value matches mine?"** — where "matches" is the condition
you write after `ON`. Matching pairs get glued side by side into one wide result row.

### INNER JOIN — "only the matches"

```sql
SELECT Orders.order_id, Customers.name, Orders.amount
FROM Orders
INNER JOIN Customers ON Orders.customer_id = Customers.customer_id;
```

Let's execute this BY HAND, row by row, exactly like the database does:

- Order 101 has customer_id 1 → is there a customer 1? **Yes, Ana** → glue them → keep.
- Order 102 has customer_id 1 → customer 1? **Yes, Ana again** → glue → keep.
- Order 103 has customer_id 3 → customer 3? **Yes, Carla** → glue → keep.

Result:

| order_id | name  | amount |
|----------|-------|--------|
| 101      | Ana   | 250    |
| 102      | Ana   | 100    |
| 103      | Carla | 80     |

Notice who is absent: **Bruno and Diego**. They have no orders, so there was nothing to
glue them to — and INNER JOIN's rule is *"no match, no row."* They simply vanish from
the result. Hold onto that, because it's exactly the gap LEFT JOIN exists to fix.

### LEFT JOIN — "everyone from the left table, matched or not"

First, what "left" means — it's literal reading order. In
`FROM Customers LEFT JOIN Orders`, **Customers is the left table** (it was written
first). LEFT JOIN's rule: **every row of the left table appears in the result, no
matter what.** If a left row finds matches, they're glued on as usual. If it finds
none, it STILL appears — with the right-side columns filled with **NULL** ("no value
exists here").

```sql
SELECT Customers.name, Orders.order_id, Orders.amount
FROM Customers
LEFT JOIN Orders ON Customers.customer_id = Orders.customer_id;
```

By hand again — this time we walk the LEFT table (Customers), one row at a time:

- Ana (1) → orders with customer_id 1? **Two of them** → Ana appears twice, once per order.
- Bruno (2) → orders with customer_id 2? **None** → Bruno appears ANYWAY, order columns = NULL.
- Carla (3) → one match → appears once, glued to order 103.
- Diego (4) → none → appears anyway, NULLs.

Result:

| name  | order_id | amount |
|-------|----------|--------|
| Ana   | 101      | 250    |
| Ana   | 102      | 100    |
| Bruno | NULL     | NULL   |
| Carla | 103      | 80     |
| Diego | NULL     | NULL   |

Put the two results side by side and the entire difference is visible: **INNER JOIN
dropped Bruno and Diego; LEFT JOIN kept them with NULLs.** That's it. That's the whole
distinction people struggle with, in one comparison.

### Why would anyone WANT the NULL rows?

Because sometimes the no-matches ARE the answer. "Which customers have never ordered?"
is a completely natural business question — and for a QA engineer, it's literally a
test scenario ("verify the app handles customers with zero orders"). The query:

```sql
SELECT Customers.name
FROM Customers
LEFT JOIN Orders ON Customers.customer_id = Orders.customer_id
WHERE Orders.order_id IS NULL;
```

Read it as: "keep everyone (LEFT JOIN), then filter down to exactly the rows where no
order got glued on." Result: Bruno, Diego. INNER JOIN could never answer this —
it threw those rows away before you could ask.

### The other joins, in one breath

**RIGHT JOIN** is LEFT JOIN mirrored — every row of the *right* table survives. Almost
nobody uses it, because you can always swap the table order and use LEFT instead.
**FULL OUTER JOIN** keeps unmatched rows from BOTH sides. Know the names; LEFT and
INNER are what interviews actually test.

**Interview one-liner:** "INNER JOIN returns only rows that match in both tables;
LEFT JOIN returns every row of the left table, with NULLs where the right side had no
match — which is exactly how you find things like customers with no orders."

## 1.6 GROUP BY — collapsing rows into summaries

"How many orders does each customer have, and what did they spend in total?"

```sql
SELECT customer_id, COUNT(*) AS order_count, SUM(amount) AS total
FROM Orders
GROUP BY customer_id;
```

`GROUP BY customer_id` gathers rows into piles — one pile per distinct customer_id —
and then COUNT/SUM run **once per pile** instead of once over everything:

| customer_id | order_count | total |
|-------------|-------------|-------|
| 1           | 2           | 350   |
| 3           | 1           | 80    |

(Ana's pile had 2 rows; Carla's had 1. Bruno/Diego have no pile — no rows to pile up.)

## 1.7 Index — "the book index" (one paragraph, that's all it needs)

Without an index, `WHERE name = 'Carla'` forces the database to read EVERY row —
like finding a topic in a book by reading every page. An **index** on a column is a
pre-sorted lookup structure, like the index at the back of the book: jump straight to
the row. Trade-off to mention: reads on that column get fast, but every INSERT/UPDATE
gets slightly slower, because the index must be updated too. That trade-off sentence
IS the interview answer.

## 1.8 Trigger — "automatic action when a table changes"

A **trigger** is a stored rule: "when X happens to this table, automatically do Y."
Classic example: *when a row is deleted from Employees, first copy it into an
Employees_Audit table.* Nobody has to remember to do it — the database itself fires
the action on every delete. That example, told as a sentence, is a complete answer to
"what are triggers and how are they used?"

## 1.9 Self-test (answers below — actually attempt them first)

Using the same Customers/Orders tables:

1. All customers from São Paulo.
2. Every order's order_id next to the customer's NAME (not id).
3. Every customer AND their total spend — including customers who spent nothing.
4. Only the customers who have never ordered.

**Answers:**

```sql
-- 1
SELECT * FROM Customers WHERE city = 'São Paulo';

-- 2
SELECT Orders.order_id, Customers.name
FROM Orders
INNER JOIN Customers ON Orders.customer_id = Customers.customer_id;

-- 3  (LEFT JOIN so Bruno/Diego survive; their SUM comes out NULL/0)
SELECT Customers.name, SUM(Orders.amount) AS total
FROM Customers
LEFT JOIN Orders ON Customers.customer_id = Orders.customer_id
GROUP BY Customers.name;

-- 4
SELECT Customers.name
FROM Customers
LEFT JOIN Orders ON Customers.customer_id = Orders.customer_id
WHERE Orders.order_id IS NULL;
```

If #3 and #4 make sense, you understand LEFT JOIN — those two are the proof.

---

# Section 2 — Puzzles, actually explained

First, the meta-rule from people who got SAP offers: **the answer is not the product —
your narrated reasoning is.** Saying "let me try a smaller version of this problem
first" out loud is already a winning move. Watch it work below.

## 2.1 The 25 machines puzzle — built up from 3 machines

**The puzzle:** 25 machines each produce weights of exactly 1000g. One broken machine
produces 999g weights instead. You have one precise scale (shows exact grams, not a
balance). **Find the broken machine using the scale only ONCE.**

**Why the obvious approach dies immediately:** weighing one weight per machine, one at
a time, is 25 weighings. You get ONE. So one weighing must somehow contain information
about ALL 25 machines at once. How can one number say 25 things?

**Shrink it: 3 machines.** Here's the trick — take a DIFFERENT number of weights from
each machine:

- 1 weight from machine A
- 2 weights from machine B
- 3 weights from machine C

Pile all 6 on the scale, one weighing. If every machine were perfect, the pile would
weigh 1+2+3 = 6 weights × 1000g = **6000g exactly.**

Now the payoff — each possible culprit produces a DIFFERENT shortfall, because each
contributed a different NUMBER of (999g) weights:

| Broken machine | Light weights in pile | Scale reads | Missing |
|---|---|---|---|
| A | 1 | 5999g | 1g |
| B | 2 | 5998g | 2g |
| C | 3 | 5997g | 3g |

**The missing grams literally spell out the machine's number.** Scale says 5998 →
2g missing → machine B. One weighing, three machines distinguished — because the
unequal counts made every outcome unique. If you'd taken 2 from each machine, every
culprit would produce the same shortfall and the reading would tell you nothing.
The inequality IS the trick.

**Scale back up to 25:** take 1 from machine 1, 2 from machine 2, ... 25 from machine
25. That's 325 weights; a perfect pile weighs 325,000g. Weigh once. Missing 17g?
    **Machine 17 is broken.** Same trick, bigger numbers.

**The transferable insight** (worth saying in an interview): when one measurement must
identify one culprit among many, make each suspect's contribution UNIQUE, so each
possible answer leaves a different fingerprint on the single reading.

## 2.2 The 10 balls puzzle — and the "three outcomes" insight

**The puzzle:** 10 identical-looking balls; one is heavier. You have a **balance
scale** — two pans, no numbers, it only tips or balances. Minimum weighings to
guarantee finding the heavy ball?

**The key insight, before any strategy:** one use of a balance scale has exactly
**three possible outcomes** — left pan sinks, right pan sinks, or they balance. Three
outcomes = one weighing can split your suspects into three groups, and the outcome
tells you which group hides the culprit. So the winning strategy is never "half vs
half" — it's **thirds**: some on the left, some on the right, some OFF the scale.

**Warm-up: 9 balls, and why 2 weighings suffice.** Split 3 / 3 / 3.

*Weighing 1:* put 3 on the left, 3 on the right, 3 stay off.
- Left sinks → heavy ball is among the left 3.
- Right sinks → among the right 3.
- Balance → among the 3 that never touched the scale. (Balance is an answer too!)

Either way, 9 suspects just became 3. *Weighing 2:* take those 3 → 1 left, 1 right,
1 off. Tips → the sinking one. Balances → the one off the scale. **Found, in 2.**

Each weighing divided suspects by 3: 9 → 3 → 1. And that's the general law: k
weighings can distinguish at most 3^k suspects (3, 9, 27, 81...).

**Now 10 balls:** 3² = 9, and 10 > 9 — ten suspects don't fit into two weighings'
worth of outcomes. One stubborn ball overflows. So the answer is **3 weighings in the
worst case** (3³ = 27 ≥ 10, plenty). Concretely: weigh 3 vs 3 with 4 off. If a side
sinks — 3 suspects, one more weighing finishes it (2 total, lucky branch). If it
balances — 4 suspects remain, and 4 doesn't split into one weighing's three outcomes,
so you need two more (3 total, worst case).

**How to answer it live:** state the three-outcomes insight, run the 9-ball version
out loud, then show why the 10th ball forces a third weighing. That's a complete,
reasoned answer — far stronger than a memorized "three."

## 2.3 If a brand-new puzzle appears

The narration script that always applies: (1) restate the puzzle in your own words —
confirms understanding, buys thinking time; (2) say "let me try a tiny version first"
and shrink it; (3) look for the resource that's oddly limited (one weighing, one
question, one crossing) — the trick always lives inside that constraint; (4) if truly
stuck, describe the approaches you're ruling out and why. Interviewers pass people who
do step 4 well even without reaching the answer.

---

# Section 3 — API testing, minimum solid

**What an API is:** a way for programs to talk to each other directly, skipping the
screen. When the login page's Submit is clicked, the browser quietly sends a request
like "POST /login with this username+password" to the server and gets an answer back.
API testing = sending those requests yourself and checking the answers — no browser,
no clicking.

**Why it matters (the testing-pyramid answer):** API tests are much faster and far
less flaky than UI tests — no page loads, no locators, no waits. So a healthy suite
has MANY API tests and fewer UI tests, reserving Selenium/Playwright for genuine
user journeys. Saying that sentence signals seniority.

**The four HTTP methods:** GET (read data), POST (create), PUT (update/replace),
DELETE (remove). Map them to a recruitment app in your head: GET a candidate's
profile, POST a new candidate, PUT updated details, DELETE a withdrawn application.

**Status code families:** 2xx = success (200 OK, 201 Created). 4xx = *the caller*
messed up (400 malformed request, 401 not logged in, 403 logged in but not allowed,
404 doesn't exist). 5xx = *the server* messed up (500). Interview shorthand:
"4xx is my fault, 5xx is theirs."

**Tools to name:** **Postman** — a GUI app for sending API requests by hand;
exploratory API testing. **REST Assured** — the standard Java library for *automated*
API tests; it plugs into TestNG and Maven exactly like Selenium does, so one framework
can hold both UI and API suites. Honest positioning if asked: "I haven't used REST
Assured in production, but I know it's the Java standard and exactly where it would
sit in my TestNG framework." Honest + informed beats bluffing every time.

---

# Section 4 — Git: pull vs fetch (the "crisp answer" they want)

**`git fetch`** downloads the new commits from the remote repository but does NOT
touch your working code. New work is now sitting locally, visible for inspection —
your files are unchanged until you deliberately merge.

**`git pull`** = fetch + merge, in one step. Download AND immediately apply into your
current branch.

**The crisp line:** "fetch downloads without applying — look before you merge;
pull downloads and applies immediately. Fetch is the cautious two-step, pull is the
shortcut." If asked when to prefer fetch: on a shared branch where you want to review
incoming changes before they mix into local work in progress.

---

# Section 5 — Managerial & HR rounds (they eliminate people — prepare)

SAP's typical shape: online test → 1–2 technical rounds → managerial → HR. The HR
round is genuinely eliminatory and reads for honesty; both overtalking and one-word
answers land badly.

**Prepare a real answer for each — say them out loud once before the interview:**

1. **"Why SAP?"** — have something specific (their products, scale of quality
   engineering, the team's domain), not generic praise.
2. **"Do you have other offers?"** — honesty is fine; follow immediately with why
   SAP is the one you're pursuing.
3. **"Tell me about a task that wasn't finished on time and you had to make quick
   decisions."** — pick ONE true story now. Shape: situation → the call you made →
   outcome → what you'd repeat or change. Concrete beats polished.
4. **"Most difficult challenge you've faced?"** — one true story, same shape.
5. **Strengths / weaknesses / 5 years** — for the weakness, name a real one plus what
   you actively do about it; never "I'm a perfectionist."
6. **"What if we offered you a tester position rather than developer?"** — asked in a
   real SAP interview. For a Quality *Engineer* role, a strong answer embraces the
   hybrid: you write code (your framework this week is proof) and your code's purpose
   is quality. Decide your genuine stance NOW, not live in the room.
7. **Your questions for them** — prepare two. Good ones: how the QE team splits manual
   vs automation; what quality ownership looks like day to day on this team.

**The single most reported tip across successful candidates:** when stuck anywhere —
puzzle, code, SQL — never go silent and never give up instantly. Narrate. "Here's
what I'm considering... this path fails because... so let me try..." That running
commentary is what's actually being scored.

---

*Priority if time runs short: Section 1 (SQL) → Section 2 (puzzles) → Section 5 (HR
answers, 20 minutes) → Sections 3–4 (30 minutes combined).*
