# SkyDrop

SkyDrop is a small drone delivery simulator used as a general software engineering case study. Students start with a buggy version 0.1 and grow it gradually through requirements, design, architecture, code review, testing, CI/CD, security, and maintenance activities.

![SkyDrop screenshot](docs/screenshot.png)

## Run it

No install needed.

1. Download or clone this repository.
2. Open `app/skydrop.html` in Chrome, Edge, or Firefox (double-click it).
3. Click the map to place delivery orders, then press **Launch**.

The drone flies from the base (H) to each order and back to base to recharge. The red area is a no-fly zone. The dashed circle shows the safe flight range.

### Run from the VS Code terminal

The application can be opened directly, so students do not need a terminal command. If a local web server is preferred, open the repository in VS Code and run:

```powershell
python -m http.server 8765 --bind 127.0.0.1
```

Then open `http://127.0.0.1:8765/app/skydrop.html`. Press `Ctrl+C` in the terminal to stop the server. Binding to `127.0.0.1` keeps the preview local to the computer. If that port is already in use, choose another number in both the command and URL.

## Development environment

Version 0.1 has no Python or Node.js dependencies, so it does not require a virtual environment. An empty environment would add setup work without isolating any packages.

When a Python mission service or hardware interface is introduced in a later activity, create and activate an environment at that point:

```powershell
python -m venv SE-env
.\SE-env\Scripts\Activate.ps1
python -m pip install --upgrade pip
```

The repository ignores both `SE-env/` and `.venv/`; virtual-environment files must never be committed. To leave the environment, run `deactivate`.

## Publish to GitHub

Create an empty GitHub repository without a generated README, license, or `.gitignore`. Then run these commands once from the VS Code terminal, replacing `YOUR-USERNAME`:

```powershell
git remote add origin https://github.com/YOUR-USERNAME/skydrop.git
git push -u origin main
```

Before pushing, run `git status`. The local `instructor/` folder should not appear because it is ignored. Never force-add it.

For later updates:

```powershell
git checkout -b feature/short-description
git add .
git commit -m "Describe the change"
git push -u origin feature/short-description
```

Open a pull request on GitHub, review the change, and merge it into `main`.

## Repository layout

```
app/                    The application (one HTML file for now)
docs/requirements.md    Current requirements, with IDs for traceability
docs/ROADMAP.md         Weekly labs and the research track
labs/                   One folder per activity with Markdown instructions
.github/ISSUE_TEMPLATE  Bug report template used in the labs
CHANGELOG.md            What changed in each version
CONTRIBUTING.md         How to branch, commit, and open a pull request
```

## Labs

| Lab | Topic | Folder |
|---|---|---|
| 1 | Debugging and bug reports | [labs/lab01-debugging](labs/lab01-debugging) |

More labs are added during the term. See [docs/ROADMAP.md](docs/ROADMAP.md).

## Version

Current version: **0.1** (contains known bugs on purpose; see Lab 1).

## License

MIT. See [LICENSE](LICENSE).
