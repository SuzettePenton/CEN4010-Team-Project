# Sprint 4 Shopping Cart REST API

This increment advances the Sprint 3 shopping cart from temporary in-memory map storage to repository-backed persistence. The four required endpoints remain unchanged so a web or mobile client can add, list, total, and delete books by user ID.

## Sprint 4 progress

- Persists cart membership through Spring Data JPA.
- Uses a file-backed H2 database for a turnkey local demonstration.
- Includes a MySQL profile for integration with the Team 8 application.
- Prevents duplicate user/book rows with service logic and a database constraint.
- Keeps different users' carts separate.
- Includes three integration tests using an isolated H2 test database.
- Preserves the professor-required HTTP behavior and response data.

`BookCatalog` remains a temporary adapter containing three sample books. During group integration, replace it with the team's existing Book service or repository rather than adding a second Book entity.

## Run locally

Requirements: Java 17 and Maven.

```powershell
mvn test
mvn spring-boot:run
```

The API runs on port 8081. Open `requests/cart-api.http` in VS Code and click each **Send Request** link from top to bottom.

Cart data now survives an application restart. To reset the local H2 data, stop the application and delete the project's `data` folder.

## Required endpoints

| Requirement | Request | Success |
| --- | --- | --- |
| Add a book | `POST /api/cart?userId=407&bookId=1` | `201 Created` |
| List books | `GET /api/cart?userId=407` | `200 OK` plus Book objects |
| Get subtotal | `GET /api/cart/subtotal?userId=407` | `200 OK` plus subtotal |
| Delete a book | `DELETE /api/cart?userId=407&bookId=1` | `204 No Content` |

## MySQL profile

Use environment variables instead of committing credentials:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/team8_bookstore?createDatabaseIfNotExist=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your-local-password"
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

The group repository may already define its datasource. If so, keep the group's configuration and copy only the cart-specific code and any dependency that is genuinely missing.

## Before merging

Read `INTEGRATION_GUIDE.md`. A standalone test result is not enough: adapt the package name, connect the real Book feature, run the full team test suite, open a pull request, obtain review, and merge through GitHub.

## Academic accuracy

This increment was prepared with AI assistance. Review and understand the code, run the tests yourself, and describe only work that has actually been committed, reviewed, or merged.

