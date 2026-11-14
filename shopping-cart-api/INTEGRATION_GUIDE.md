# Sprint 4 GitHub Integration Guide

## Recommended branch plan

Use the team's actual default branch name. Replace `master` with `main` in every command if the repository uses `main`.

```powershell
git switch master
git pull origin master
git switch -c feature/shopping-cart-sprint4
```

If the feature branch already exists remotely:

```powershell
git fetch origin
git switch feature/shopping-cart-sprint4
git pull origin feature/shopping-cart-sprint4
git merge master
```

## Integrate the cart code

Do not copy the standalone folder over the group repository. Instead:

1. Copy the cart controller, service, repository, entity, response, exceptions, and tests into matching source folders in the group project.
2. Change `package edu.fiu.team8.sprint4;` to the group's existing base package.
3. Replace `BookCatalog` with the group Book service/repository and map its Book type to the required response.
4. If the group already has a User entity, validate `userId` through that feature instead of creating a duplicate User class.
5. Add `spring-boot-starter-data-jpa` only if the group `pom.xml` does not already contain it.
6. Keep the group's existing MySQL datasource configuration and credentials policy.
7. Resolve endpoint conflicts before committing.

## Verify before pushing

```powershell
mvn test
git status
git diff
```

Also start the complete application and send all requests in `requests/cart-api.http`. Confirm that other team features still work.

## Commit and push

```powershell
git add src/main src/test requests pom.xml
git commit -m "Persist shopping cart items and add integration tests"
git push -u origin feature/shopping-cart-sprint4
```

Only stage `pom.xml` if you intentionally changed it. Review `git status` so unrelated files are not committed.

## Pull request and merge

1. Open GitHub and create a pull request from `feature/shopping-cart-sprint4` into `master` or `main`.
2. Describe the four endpoints, JPA persistence, tests run, and remaining Book/User integration assumptions.
3. Ask at least one teammate to review the code.
4. Address conflicts or requested changes in the feature branch.
5. Re-run the complete test suite after the branch is current with the default branch.
6. Merge the pull request only when the team build passes.
7. Delete the remote feature branch if the team normally cleans merged branches.

Update your local default branch:

```powershell
git switch master
git pull origin master
```

## Evidence to save for Sprint 4

- Feature branch visible in GitHub.
- Commit containing the cart changes.
- Passing Maven test summary.
- Pull request and review.
- Merge commit or merged pull request page.
- Working REST responses from the integrated default branch.

Do not mark the merge story Done in the Sprint Review until the pull request is actually merged.
