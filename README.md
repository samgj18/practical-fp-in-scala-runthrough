# Shopping Cart

<a href="https://typelevel.org/cats/"><img src="https://raw.githubusercontent.com/typelevel/cats/c23130d2c2e4a320ba4cde9a7c7895c6f217d305/docs/src/main/resources/microsite/img/cats-badge.svg" height="40px" align="right" alt="Cats friendly" /></a>

## Components Overview

![arch](https://user-images.githubusercontent.com/39674930/147899354-d238be67-fba8-4ee4-876a-14b526bb3eca.png)

- Both **Services** and **Authentication** are _algebras_. The latter are mainly dependencies for some of the services.
- Programs shows **Checkout**, the business logic that combines most of the services.
- Effects show our custom interfaces required implicitly.
- The lines connecting services to Redis and PostgreSQL show which ones access which storage.
- The HTTP layer shows the client and the different routes.
- At the very end, we have both the modules and the entry point to the application.

## Run

To spin up locally (on Mac) run the following commands:

- `docker-compose up`
- ```shell
  export DEBUG=false &&
  export SC_ACCESS_TOKEN_SECRET_KEY=5h0pp1ng_k4rt &&
  export SC_JWT_SECRET_KEY=-*5h0pp1ng_k4rt*- &&
  export SC_JWT_CLAIM="{\"uuid\": \"004b4457-71c3-4439-a1b2-03820263b59c\"}" &&
  export SC_ADMIN_USER_TOKEN=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjogIjAwNGI0NDU3LTcxYzMtNDQzOS1hMWIyLTAzODIwMjYzYjU5YyJ9.L97BnPScSAKY-BLkYu8G_n8h1U4LDOURUen14O22hD4 &&
  export SC_PASSWORD_SALT=06grsnxXG0d*Pj496p6fuA*o &&
  export SC_APP_ENV=test &&
  export SC_POSTGRES_PASSWORD=my-password
  ```

- `sbt reStart`

## Tests

To run Unit Tests:

```

sbt test

```

To run Integration Tests we need to run both `PostgreSQL` and `Redis`:

```

docker-compose up
sbt it:test
docker-compose down

```

```

```

**For more information go to [Gabriel's Volpe guide](https://github.com/gvolpe/pfps-shopping-cart)**
