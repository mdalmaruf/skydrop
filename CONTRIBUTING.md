# Contributing to SkyDrop

## Workflow

1. Create a branch for each change: `git checkout -b fix/fifo-queue` or `feature/priority-orders`.
2. Make small commits with clear messages, for example: `Fix FR-2: deliver orders first come, first served`.
3. Push the branch and open a pull request.
4. Ask a teammate to review it. Fix the review comments.
5. Merge only after the review is approved (and, from week 10, the tests pass).

## Bug reports

Use the **Bug report** issue template. A good bug report lets someone else reproduce the bug quickly. Include the requirement ID it breaks (see `docs/requirements.md`).

## Code style

- Keep functions short and named for what they do.
- Refer to requirement IDs in comments where a rule is enforced, e.g. `// FR-3`.
- Do not mix drawing code and flight logic (this becomes strict after the architecture lab).
