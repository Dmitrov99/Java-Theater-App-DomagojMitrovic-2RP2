-- ============================================================
-- KazalisteDb - dummy / seed data
-- Pokrenuti NAKON initialization / DDL skripte.
--
-- Namjena:
-- - lokalni razvoj
-- - testiranje CRUD-a, TableView prikaza, pretrage i repertoara
-- - testiranje castova predstava (play_actor)
-- - testiranje zaposlenih glumaca i redatelja po kazalištu
--
-- Napomena o lozinkama:
-- Vrijednosti password_hash u ovoj skripti su obične testne
-- lozinke, samo za lokalni razvoj. Nisu sigurni hash-evi.
-- ============================================================

\set ON_ERROR_STOP on

BEGIN;

-- ============================================================
-- APP USERS
-- DDL već umeće:
-- - postgres / secret / ADMIN
-- - korisnik / korisnik123 / USER
--
-- Ovdje dodajemo dodatne korisnike za testiranje.
-- ============================================================

INSERT INTO app_user (username, password_hash, role)
VALUES
    ('admin', 'admin', 'ADMIN'),
    ('korisnik', 'korisnik123', 'USER'),
    ('admin', 'admin123', 'ADMIN'),
    ('ana.user', 'ana123', 'USER'),
    ('marko.user', 'marko123', 'USER'),
    ('ivana.user', 'ivana123', 'USER'),
    ('luka.user', 'luka123', 'USER'),
    ('petra.user', 'petra123', 'USER')
ON CONFLICT (username) DO NOTHING;


-- ============================================================
-- COUNTRIES
-- ============================================================

INSERT INTO country (country_code, state_name)
VALUES
    ('HR', 'Hrvatska'),
    ('SI', 'Slovenija'),
    ('AT', 'Austrija')
ON CONFLICT (country_code) DO NOTHING;


-- ============================================================
-- CITIES
-- ============================================================

INSERT INTO city (name, postal_code)
VALUES
    ('Zagreb', '10000'),
    ('Split', '21000'),
    ('Rijeka', '51000'),
    ('Osijek', '31000'),
    ('Varaždin', '42000'),
    ('Zadar', '23000'),
    ('Dubrovnik', '20000'),
    ('Pula', '52100'),
    ('Šibenik', '22000'),
    ('Karlovac', '47000'),
    ('Ljubljana', '1000'),
    ('Beč', '1010')
ON CONFLICT (name, postal_code) DO NOTHING;


-- ============================================================
-- ACTORS
-- OIB mora sadržavati točno 11 znamenki.
-- actor_id mora biti jedinstven.
-- ============================================================

INSERT INTO actor (first_name, last_name, oib, actor_id)
VALUES
    ('Marko', 'Cindrić', '10000000001', 'ACT-001'),
    ('Nina', 'Violić', '10000000002', 'ACT-002'),
    ('Goran', 'Bogdan', '10000000003', 'ACT-003'),
    ('Tihana', 'Lazović', '10000000004', 'ACT-004'),
    ('Filip', 'Šovagović', '10000000005', 'ACT-005'),
    ('Ksenija', 'Marinković', '10000000006', 'ACT-006'),
    ('Rakan', 'Rushaidat', '10000000007', 'ACT-007'),
    ('Nataša', 'Janjić Medančić', '10000000008', 'ACT-008'),
    ('Milan', 'Pleština', '10000000009', 'ACT-009'),
    ('Lucija', 'Šerbedžija', '10000000010', 'ACT-010'),
    ('Ivan', 'Glowatzky', '10000000011', 'ACT-011'),
    ('Petra', 'Kralj', '10000000012', 'ACT-012'),
    ('Dario', 'Vuković', '10000000013', 'ACT-013'),
    ('Mia', 'Radić', '10000000014', 'ACT-014'),
    ('Lovro', 'Babić', '10000000015', 'ACT-015'),
    ('Ema', 'Horvat', '10000000016', 'ACT-016'),
    ('Karlo', 'Novak', '10000000017', 'ACT-017'),
    ('Sara', 'Kovačević', '10000000018', 'ACT-018')
ON CONFLICT (oib) DO NOTHING;


-- ============================================================
-- DIRECTORS
-- direction_style koristi postojeći PostgreSQL enum:
-- DRAMA, COMEDY, MUSICAL, OPERA, BALLET
-- ============================================================

INSERT INTO director (
    first_name,
    last_name,
    oib,
    director_id,
    direction_style
)
VALUES
    ('Ivica', 'Buljan', '20000000001', 'DIR-001', 'DRAMA'),
    ('Krešimir', 'Dolenčić', '20000000002', 'DIR-002', 'MUSICAL'),
    ('Saša', 'Anočić', '20000000003', 'DIR-003', 'COMEDY'),
    ('Dora', 'Ruždjak Podolski', '20000000004', 'DIR-004', 'DRAMA'),
    ('Marin', 'Blažević', '20000000005', 'DIR-005', 'OPERA'),
    ('Staša', 'Zurovac', '20000000006', 'DIR-006', 'BALLET'),
    ('Marta', 'Kovač', '20000000007', 'DIR-007', 'COMEDY'),
    ('Nikola', 'Jurić', '20000000008', 'DIR-008', 'DRAMA'),
    ('Petar', 'Matić', '20000000009', 'DIR-009', 'MUSICAL')
ON CONFLICT (oib) DO NOTHING;


-- ============================================================
-- THEATERS
-- FK vrijednosti country_id i city_id dohvaćaju se preko
-- jedinstvenih business vrijednosti, bez pretpostavljenih ID-jeva.
-- ============================================================

INSERT INTO theater (
    name,
    address,
    founded_year,
    auditorium_capacity,
    history,
    image_path,
    country_id,
    city_id
)
VALUES
    (
        'Hrvatsko narodno kazalište u Zagrebu',
        'Trg Republike Hrvatske 15, Zagreb',
        1895,
        809,
        'Jedno od središnjih hrvatskih kazališta s dramskim, opernim i baletnim programom.',
        '/images/hnk-zagreb.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Zagreb' AND postal_code = '10000')
    ),
    (
        'Kazalište Kerempuh',
        'Ilica 31, Zagreb',
        1964,
        500,
        'Gradsko satiričko kazalište poznato po komedijama, satiri i društveno angažiranim predstavama.',
        '/images/kerempuh.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Zagreb' AND postal_code = '10000')
    ),
    (
        'Gradsko dramsko kazalište Gavella',
        'Frankopanska 10, Zagreb',
        1953,
        450,
        'Zagrebačko dramsko kazalište s naglaskom na suvremeni i klasični repertoar.',
        '/images/Gavella.JPG',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Zagreb' AND postal_code = '10000')
    ),
    (
        'Hrvatsko narodno kazalište Split',
        'Trg Gaje Bulata 1, Split',
        1893,
        1000,
        'Nacionalno kazalište u Splitu koje objedinjuje dramu, operu i balet.',
        '/images/hnk-split.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Split' AND postal_code = '21000')
    ),
    (
        'Hrvatsko narodno kazalište Ivana pl. Zajca',
        'Uljarska 1, Rijeka',
        1885,
        650,
        'Riječko nacionalno kazalište s programima hrvatske drame, talijanske drame, opere i baleta.',
        '/images/hnk-rijeka.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Rijeka' AND postal_code = '51000')
    ),
    (
        'Hrvatsko narodno kazalište u Osijeku',
        'Županijska 9, Osijek',
        1907,
        550,
        'Kazalište s dugom tradicijom dramskih, opernih i glazbeno-scenskih produkcija.',
        '/images/hnk-osijek.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Osijek' AND postal_code = '31000')
    ),
    (
        'Hrvatsko narodno kazalište u Varaždinu',
        'Augusta Cesarca 1, Varaždin',
        1873,
        370,
        'Povijesno varaždinsko kazalište s bogatim dramskim i glazbenim programom.',
        '/images/hnk-varazdin.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Varaždin' AND postal_code = '42000')
    ),
    (
        'Kazalište lutaka Zadar',
        'Sokolská 1, Zadar',
        1951,
        220,
        'Kazalište za djecu i mlade s lutkarskim, glumačkim i glazbenim predstavama.',
        '/images/kazaliste-lutaka-zadar.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Zadar' AND postal_code = '23000')
    ),
    (
        'Kazalište Marina Držića',
        'Pred Dvorom 1, Dubrovnik',
        1944,
        320,
        'Dubrovačko gradsko kazalište posvećeno dramskoj baštini i suvremenim produkcijama.',
        '/images/kazaliste-marina.drzica.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Dubrovnik' AND postal_code = '20000')
    ),
    (
        'Istarsko narodno kazalište',
        'Laginjina 5, Pula',
        1989,
        400,
        'Pulsko kazalište koje izvodi dramske, glazbene i plesne programe.',
        '/images/Istarsko narodno kazalište.jpg',
        (SELECT id FROM country WHERE country_code = 'HR'),
        (SELECT id FROM city WHERE name = 'Pula' AND postal_code = '52100')
    )
ON CONFLICT (name, address) DO NOTHING;


-- ============================================================
-- THEATER - ACTOR
-- Zaposleni / povezani glumci po kazalištu.
-- ============================================================

INSERT INTO theater_actor (theater_id, actor_id)
VALUES
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-001')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-002')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-005')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-006')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište Kerempuh'
           AND address = 'Ilica 31, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-007')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište Kerempuh'
           AND address = 'Ilica 31, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-008')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište Kerempuh'
           AND address = 'Ilica 31, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-012')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Gradsko dramsko kazalište Gavella'
           AND address = 'Frankopanska 10, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-003')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Gradsko dramsko kazalište Gavella'
           AND address = 'Frankopanska 10, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-004')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Gradsko dramsko kazalište Gavella'
           AND address = 'Frankopanska 10, Zagreb'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-011')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Split'
           AND address = 'Trg Gaje Bulata 1, Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-009')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Split'
           AND address = 'Trg Gaje Bulata 1, Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-010')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Split'
           AND address = 'Trg Gaje Bulata 1, Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-013')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'
           AND address = 'Uljarska 1, Rijeka'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-001')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'
           AND address = 'Uljarska 1, Rijeka'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-014')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'
           AND address = 'Uljarska 1, Rijeka'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-015')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Osijeku'
           AND address = 'Županijska 9, Osijek'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-016')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Osijeku'
           AND address = 'Županijska 9, Osijek'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-017')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Varaždinu'
           AND address = 'Augusta Cesarca 1, Varaždin'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-018')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište lutaka Zadar'
           AND address = 'Sokolská 1, Zadar'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-012')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište Marina Držića'
           AND address = 'Pred Dvorom 1, Dubrovnik'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-003')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Istarsko narodno kazalište'
           AND address = 'Laginjina 5, Pula'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-004')
    )
ON CONFLICT (theater_id, actor_id) DO NOTHING;


-- ============================================================
-- THEATER - DIRECTOR
-- Zaposleni / povezani redatelji po kazalištu.
-- ============================================================

INSERT INTO theater_director (theater_id, director_id)
VALUES
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        (SELECT id FROM director WHERE director_id = 'DIR-001')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        (SELECT id FROM director WHERE director_id = 'DIR-002')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        (SELECT id FROM director WHERE director_id = 'DIR-006')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište Kerempuh'
           AND address = 'Ilica 31, Zagreb'),
        (SELECT id FROM director WHERE director_id = 'DIR-003')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište Kerempuh'
           AND address = 'Ilica 31, Zagreb'),
        (SELECT id FROM director WHERE director_id = 'DIR-007')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Gradsko dramsko kazalište Gavella'
           AND address = 'Frankopanska 10, Zagreb'),
        (SELECT id FROM director WHERE director_id = 'DIR-004')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Gradsko dramsko kazalište Gavella'
           AND address = 'Frankopanska 10, Zagreb'),
        (SELECT id FROM director WHERE director_id = 'DIR-008')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Split'
           AND address = 'Trg Gaje Bulata 1, Split'),
        (SELECT id FROM director WHERE director_id = 'DIR-005')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Split'
           AND address = 'Trg Gaje Bulata 1, Split'),
        (SELECT id FROM director WHERE director_id = 'DIR-009')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'
           AND address = 'Uljarska 1, Rijeka'),
        (SELECT id FROM director WHERE director_id = 'DIR-001')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'
           AND address = 'Uljarska 1, Rijeka'),
        (SELECT id FROM director WHERE director_id = 'DIR-006')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Osijeku'
           AND address = 'Županijska 9, Osijek'),
        (SELECT id FROM director WHERE director_id = 'DIR-002')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Varaždinu'
           AND address = 'Augusta Cesarca 1, Varaždin'),
        (SELECT id FROM director WHERE director_id = 'DIR-003')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište lutaka Zadar'
           AND address = 'Sokolská 1, Zadar'),
        (SELECT id FROM director WHERE director_id = 'DIR-007')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Kazalište Marina Držića'
           AND address = 'Pred Dvorom 1, Dubrovnik'),
        (SELECT id FROM director WHERE director_id = 'DIR-008')
    ),
    (
        (SELECT id FROM theater
         WHERE name = 'Istarsko narodno kazalište'
           AND address = 'Laginjina 5, Pula'),
        (SELECT id FROM director WHERE director_id = 'DIR-004')
    )
ON CONFLICT (theater_id, director_id) DO NOTHING;


-- ============================================================
-- PLAYS
-- Svaka predstava pripada jednom kazalištu i ima jednog redatelja.
-- ============================================================

INSERT INTO play (
    name,
    director_id,
    theater_id,
    premier_date,
    performance_counter,
    play_type
)
VALUES
    (
        'Hamlet',
        (SELECT id FROM director WHERE director_id = 'DIR-001'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        DATE '2025-02-14',
        38,
        'DRAMA'
    ),
    (
        'Labuđe jezero',
        (SELECT id FROM director WHERE director_id = 'DIR-006'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        DATE '2025-05-23',
        19,
        'BALLET'
    ),
    (
        'Jadnici',
        (SELECT id FROM director WHERE director_id = 'DIR-002'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Zagrebu'
           AND address = 'Trg Republike Hrvatske 15, Zagreb'),
        DATE '2024-11-08',
        47,
        'MUSICAL'
    ),
    (
        'Predstava Hamleta u selu Mrduša Donja',
        (SELECT id FROM director WHERE director_id = 'DIR-003'),
        (SELECT id FROM theater
         WHERE name = 'Kazalište Kerempuh'
           AND address = 'Ilica 31, Zagreb'),
        DATE '2025-01-17',
        34,
        'COMEDY'
    ),
    (
        'Ustav Republike Hrvatske',
        (SELECT id FROM director WHERE director_id = 'DIR-007'),
        (SELECT id FROM theater
         WHERE name = 'Kazalište Kerempuh'
           AND address = 'Ilica 31, Zagreb'),
        DATE '2025-09-12',
        8,
        'COMEDY'
    ),
    (
        'Tko se boji Virginije Woolf?',
        (SELECT id FROM director WHERE director_id = 'DIR-004'),
        (SELECT id FROM theater
         WHERE name = 'Gradsko dramsko kazalište Gavella'
           AND address = 'Frankopanska 10, Zagreb'),
        DATE '2024-10-04',
        51,
        'DRAMA'
    ),
    (
        'Kralj Edip',
        (SELECT id FROM director WHERE director_id = 'DIR-008'),
        (SELECT id FROM theater
         WHERE name = 'Gradsko dramsko kazalište Gavella'
           AND address = 'Frankopanska 10, Zagreb'),
        DATE '2025-03-28',
        27,
        'DRAMA'
    ),
    (
        'Carmen',
        (SELECT id FROM director WHERE director_id = 'DIR-005'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Split'
           AND address = 'Trg Gaje Bulata 1, Split'),
        DATE '2025-06-06',
        22,
        'OPERA'
    ),
    (
        'Mamma Mia!',
        (SELECT id FROM director WHERE director_id = 'DIR-009'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Split'
           AND address = 'Trg Gaje Bulata 1, Split'),
        DATE '2024-12-20',
        41,
        'MUSICAL'
    ),
    (
        'Gospoda Glembajevi',
        (SELECT id FROM director WHERE director_id = 'DIR-001'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'
           AND address = 'Uljarska 1, Rijeka'),
        DATE '2025-02-07',
        29,
        'DRAMA'
    ),
    (
        'Orašar',
        (SELECT id FROM director WHERE director_id = 'DIR-006'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'
           AND address = 'Uljarska 1, Rijeka'),
        DATE '2024-12-06',
        44,
        'BALLET'
    ),
    (
        'Ero s onoga svijeta',
        (SELECT id FROM director WHERE director_id = 'DIR-002'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Osijeku'
           AND address = 'Županijska 9, Osijek'),
        DATE '2025-04-11',
        18,
        'MUSICAL'
    ),
    (
        'Revizor',
        (SELECT id FROM director WHERE director_id = 'DIR-003'),
        (SELECT id FROM theater
         WHERE name = 'Hrvatsko narodno kazalište u Varaždinu'
           AND address = 'Augusta Cesarca 1, Varaždin'),
        DATE '2025-05-09',
        16,
        'COMEDY'
    ),
    (
        'Čarobnjak iz Oza',
        (SELECT id FROM director WHERE director_id = 'DIR-007'),
        (SELECT id FROM theater
         WHERE name = 'Kazalište lutaka Zadar'
           AND address = 'Sokolská 1, Zadar'),
        DATE '2025-03-15',
        25,
        'COMEDY'
    ),
    (
        'Dundo Maroje',
        (SELECT id FROM director WHERE director_id = 'DIR-008'),
        (SELECT id FROM theater
         WHERE name = 'Kazalište Marina Držića'
           AND address = 'Pred Dvorom 1, Dubrovnik'),
        DATE '2025-07-04',
        14,
        'DRAMA'
    ),
    (
        'San ljetne noći',
        (SELECT id FROM director WHERE director_id = 'DIR-004'),
        (SELECT id FROM theater
         WHERE name = 'Istarsko narodno kazalište'
           AND address = 'Laginjina 5, Pula'),
        DATE '2025-08-22',
        10,
        'DRAMA'
    )
ON CONFLICT (theater_id, name, premier_date) DO NOTHING;


-- ============================================================
-- PLAY - ACTOR
-- Cast predstava za prikaz i drag-and-drop testiranje.
-- Svaki play ima između 2 i 4 glumca.
-- ============================================================

INSERT INTO play_actor (play_id, actor_id)
VALUES
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Hamlet'
           AND p.premier_date = DATE '2025-02-14'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-001')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Hamlet'
           AND p.premier_date = DATE '2025-02-14'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-002')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Hamlet'
           AND p.premier_date = DATE '2025-02-14'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-005')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Labuđe jezero'
           AND p.premier_date = DATE '2025-05-23'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-006')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Labuđe jezero'
           AND p.premier_date = DATE '2025-05-23'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-002')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Jadnici'
           AND p.premier_date = DATE '2024-11-08'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-001')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Jadnici'
           AND p.premier_date = DATE '2024-11-08'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-005')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Jadnici'
           AND p.premier_date = DATE '2024-11-08'
           AND t.name = 'Hrvatsko narodno kazalište u Zagrebu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-006')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Predstava Hamleta u selu Mrduša Donja'
           AND p.premier_date = DATE '2025-01-17'
           AND t.name = 'Kazalište Kerempuh'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-007')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Predstava Hamleta u selu Mrduša Donja'
           AND p.premier_date = DATE '2025-01-17'
           AND t.name = 'Kazalište Kerempuh'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-008')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Ustav Republike Hrvatske'
           AND p.premier_date = DATE '2025-09-12'
           AND t.name = 'Kazalište Kerempuh'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-007')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Ustav Republike Hrvatske'
           AND p.premier_date = DATE '2025-09-12'
           AND t.name = 'Kazalište Kerempuh'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-012')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Tko se boji Virginije Woolf?'
           AND p.premier_date = DATE '2024-10-04'
           AND t.name = 'Gradsko dramsko kazalište Gavella'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-003')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Tko se boji Virginije Woolf?'
           AND p.premier_date = DATE '2024-10-04'
           AND t.name = 'Gradsko dramsko kazalište Gavella'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-004')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Tko se boji Virginije Woolf?'
           AND p.premier_date = DATE '2024-10-04'
           AND t.name = 'Gradsko dramsko kazalište Gavella'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-011')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Kralj Edip'
           AND p.premier_date = DATE '2025-03-28'
           AND t.name = 'Gradsko dramsko kazalište Gavella'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-003')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Kralj Edip'
           AND p.premier_date = DATE '2025-03-28'
           AND t.name = 'Gradsko dramsko kazalište Gavella'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-011')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Carmen'
           AND p.premier_date = DATE '2025-06-06'
           AND t.name = 'Hrvatsko narodno kazalište Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-009')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Carmen'
           AND p.premier_date = DATE '2025-06-06'
           AND t.name = 'Hrvatsko narodno kazalište Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-010')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Carmen'
           AND p.premier_date = DATE '2025-06-06'
           AND t.name = 'Hrvatsko narodno kazalište Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-013')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Mamma Mia!'
           AND p.premier_date = DATE '2024-12-20'
           AND t.name = 'Hrvatsko narodno kazalište Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-009')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Mamma Mia!'
           AND p.premier_date = DATE '2024-12-20'
           AND t.name = 'Hrvatsko narodno kazalište Split'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-010')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Gospoda Glembajevi'
           AND p.premier_date = DATE '2025-02-07'
           AND t.name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-001')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Gospoda Glembajevi'
           AND p.premier_date = DATE '2025-02-07'
           AND t.name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-014')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Gospoda Glembajevi'
           AND p.premier_date = DATE '2025-02-07'
           AND t.name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-015')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Orašar'
           AND p.premier_date = DATE '2024-12-06'
           AND t.name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-014')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Orašar'
           AND p.premier_date = DATE '2024-12-06'
           AND t.name = 'Hrvatsko narodno kazalište Ivana pl. Zajca'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-015')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Ero s onoga svijeta'
           AND p.premier_date = DATE '2025-04-11'
           AND t.name = 'Hrvatsko narodno kazalište u Osijeku'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-016')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Ero s onoga svijeta'
           AND p.premier_date = DATE '2025-04-11'
           AND t.name = 'Hrvatsko narodno kazalište u Osijeku'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-017')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Revizor'
           AND p.premier_date = DATE '2025-05-09'
           AND t.name = 'Hrvatsko narodno kazalište u Varaždinu'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-018')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Čarobnjak iz Oza'
           AND p.premier_date = DATE '2025-03-15'
           AND t.name = 'Kazalište lutaka Zadar'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-012')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Čarobnjak iz Oza'
           AND p.premier_date = DATE '2025-03-15'
           AND t.name = 'Kazalište lutaka Zadar'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-018')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Dundo Maroje'
           AND p.premier_date = DATE '2025-07-04'
           AND t.name = 'Kazalište Marina Držića'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-003')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'Dundo Maroje'
           AND p.premier_date = DATE '2025-07-04'
           AND t.name = 'Kazalište Marina Držića'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-011')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'San ljetne noći'
           AND p.premier_date = DATE '2025-08-22'
           AND t.name = 'Istarsko narodno kazalište'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-004')
    ),
    (
        (SELECT p.id
         FROM play p
                  JOIN theater t ON t.id = p.theater_id
         WHERE p.name = 'San ljetne noći'
           AND p.premier_date = DATE '2025-08-22'
           AND t.name = 'Istarsko narodno kazalište'),
        (SELECT id FROM actor WHERE actor_id = 'ACT-014')
    )
ON CONFLICT (play_id, actor_id) DO NOTHING;

COMMIT;


-- ============================================================
-- Optional verification queries
-- Pokreni ručno nakon seedanja ako želiš provjeriti rezultate.
-- ============================================================

-- SELECT COUNT(*) AS broj_korisnika FROM app_user;
-- SELECT COUNT(*) AS broj_drzava FROM country;
-- SELECT COUNT(*) AS broj_gradova FROM city;
-- SELECT COUNT(*) AS broj_glumaca FROM actor;
-- SELECT COUNT(*) AS broj_redatelja FROM director;
-- SELECT COUNT(*) AS broj_kazalista FROM theater;
-- SELECT COUNT(*) AS broj_predstava FROM play;
-- SELECT COUNT(*) AS broj_veza_predstava_glumac FROM play_actor;

-- Pregled repertoara:
-- SELECT
--     t.name AS kazaliste,
--     p.name AS predstava,
--     p.premier_date,
--     p.play_type,
--     p.performance_counter,
--     d.first_name || ' ' || d.last_name AS redatelj
-- FROM play p
-- JOIN theater t ON t.id = p.theater_id
-- JOIN director d ON d.id = p.director_id
-- ORDER BY t.name, p.premier_date DESC;

-- Pregled castova:
-- SELECT
--     t.name AS kazaliste,
--     p.name AS predstava,
--     a.first_name || ' ' || a.last_name AS glumac
-- FROM play_actor pa
-- JOIN play p ON p.id = pa.play_id
-- JOIN theater t ON t.id = p.theater_id
-- JOIN actor a ON a.id = pa.actor_id
-- ORDER BY t.name, p.name, a.last_name, a.first_name;