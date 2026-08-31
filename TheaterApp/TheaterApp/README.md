# Theater Management App

JavaFX aplikacija za upravljanje kazalištima, predstavama, glumcima i redateljima.

## Tehnologije

- Java 25
- JavaFX
- Maven
- PostgreSQL
- Docker / Docker Compose
- Jackson XML

## Pokretanje baze

Projekt koristi PostgreSQL bazu pokrenutu kroz Docker.

1. Provjerite da je Docker Desktop pokrenut.
2. U terminalu otvorite direktorij u kojem se nalazi `docker-compose.yml`.
3. Pokrenite bazu:

```bash
docker compose up -d
```

Za provjeru pokrenutih containera:

```bash
docker compose ps
```

Za gašenje baze:

```bash
docker compose down
```

## Pokretanje aplikacije

Projekt koristi Java 25.

Aplikaciju možete pokrenuti iz IntelliJ IDEA-e pokretanjem glavne JavaFX klase.

Za Maven build na macOS-u, iz direktorija koji sadrži `mvnw` datoteku pokrenite:

```bash
./mvnw clean install
```

Ako skripta nema dozvolu za pokretanje:

```bash
chmod +x mvnw
```

## Testni korisnici

| Uloga | Korisničko ime | Lozinka |
|---|---|---|
| Administrator | `admin` | `admin123` |
| Korisnik | `korisnik` | `korisnik123` |

Administrator može upravljati podacima u aplikaciji. Korisnik može pregledavati podatke.

## XML export

Administrator može izvesti repertoar odabranog kazališta u XML datoteku. Export se generira po kazališnim sezonama i sadrži predstave, redatelje i glumce.
