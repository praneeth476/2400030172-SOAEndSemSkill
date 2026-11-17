# Multi-Container Infrastructure Setup & Deployment using Docker Compose

This project directly implements the three tasks from the question:

1. Create a `docker-compose.yml` file with `app-db` and `app-service`.
2. Run `docker compose up -d --build` to build and start both containers in detached mode.
3. Test `http://localhost:8080/api/users` to verify that the application communicates with the PostgreSQL database.

## Project structure

```text
docker-compose-multicontainer-task/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── .dockerignore
├── .gitignore
├── README.md
├── commands.txt
├── expected-output.txt
└── src/
    └── main/
        ├── java/com/example/dockercompose/
        │   ├── DockerComposeApplication.java
        │   ├── controller/UserController.java
        │   ├── model/User.java
        │   ├── repository/UserRepository.java
        │   └── service/UserService.java
        └── resources/
            ├── application.properties
            └── schema.sql
```

## Requirements

- Docker Desktop / Docker Engine
- Docker Compose v2 (`docker compose`)
- Internet connection for the first image/dependency download

You do NOT need to install Java or PostgreSQL locally to run the application through Docker.

## Step 1 - Docker Compose file

The `docker-compose.yml` contains:

- `app-db` = PostgreSQL database container
- `app-service` = Spring Boot application container
- Environment variables for database configuration
- A shared Docker network
- A PostgreSQL health check
- `depends_on` so the application starts after the database becomes healthy

The important database connection is:

```text
jdbc:postgresql://app-db:5432/appdb
```

`app-db` is the Docker Compose service name. The application does not use `localhost` to reach PostgreSQL.

## Step 2 - Deploy infrastructure

Open a terminal in this project folder and run:

```bash
docker compose up -d --build
```

Check containers:

```bash
docker compose ps
```

Check application logs:

```bash
docker compose logs app-service
```

Check database logs:

```bash
docker compose logs app-db
```

## Step 3 - Test deployment

After the application starts, open:

```text
http://localhost:8080/api/users
```

Or run:

```bash
curl http://localhost:8080/api/users
```

Expected response:

```json
[
  {
    "id": 1,
    "name": "Sai"
  },
  {
    "id": 2,
    "name": "Docker User"
  }
]
```

The exact JSON formatting may differ, but the important point is that the users are read from PostgreSQL.

## Verify the two containers

Run:

```bash
docker compose ps
```

You should see both:

```text
app-service
app-db
```

Then verify the shared network:

```bash
docker network ls
```

To inspect the Compose network:

```bash
docker network inspect docker-compose-multicontainer-task_app-network
```

If the project directory name is different, the generated network name can also be different. Use:

```bash
docker compose config
```

to see the resolved configuration.

## Useful commands

Stop containers:

```bash
docker compose stop
```

Start existing containers:

```bash
docker compose start
```

Stop and remove containers:

```bash
docker compose down
```

Remove containers and database volume:

```bash
docker compose down -v
```

Rebuild:

```bash
docker compose up -d --build
```

Follow logs:

```bash
docker compose logs -f
```

## Viva Answers

### 1. Why is Docker Compose used instead of running individual `docker run` commands for multi-container deployments?

Docker Compose is used to manage multiple containers together from one configuration file. It defines services, ports, environment variables, networks, volumes, and dependencies in `docker-compose.yml`. Therefore, all containers can be created, started, stopped, and rebuilt using simple commands such as `docker compose up` and `docker compose down`.

### 2. How do containerized services communicate with each other using service names in a shared Docker network?

Docker Compose places the services on a shared Docker network. Docker provides internal DNS, so a service can reach another service using its Compose service name. In this project, the application connects to PostgreSQL using:

```text
app-db:5432
```

It does not use `localhost:5432`, because `localhost` inside the application container refers to the application container itself.

## Task-to-file mapping

- Step 1: `docker-compose.yml`, `Dockerfile`, `application.properties`
- Step 2: `commands.txt` and the `docker compose up -d --build` command
- Step 3: `UserController.java` provides `/api/users`
- Database setup: `schema.sql`
- Viva: answers are included in this README
