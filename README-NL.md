# Rijmaatje Android — startproject

Dit project is een eerste Android-omhulsel voor de Rijmaatje-webapp. Het gebruikt een lokale HTML-pagina in een Android WebView en bevat een basis-voorgrondservice voor GPS.

## Wat dit al doet
- Android-appstructuur met naam Rijmaatje.
- Opent de meegeleverde webapp lokaal, zonder internet nodig te hebben voor de HTML.
- Vraagt locatie- en meldingsrechten.
- Bevat een GPS-voorgrondservice die een blijvende melding toont en GPS-snelheid in die melding bijwerkt.

## Wat nog niet af is
- De GPS-voorgrondservice wordt gestart/gestopt wanneer je in de app GPS start/stopt. De service werkt de blijvende Android-melding bij, maar de snelheid uit die service is nog niet gekoppeld aan het grote getal in de webpagina; de pagina gebruikt daarnaast browser-GPS.
- De HTML-spraakherkenning en spraakuitvoer zijn nog niet geschikt gemaakt voor betrouwbaar gebruik met het scherm uit.
- De wakkerroep **“Hé Maatje!”** is nog niet ingebouwd. Dit vraagt een aparte wake-word-oplossing en Android-microfoonservice; gewone browser-spraakherkenning is hiervoor niet genoeg.
- Test de app eerst stilstaand en veilig. Bedien je telefoon niet tijdens het rijden.

## APK bouwen via GitHub (op je telefoon)
1. Maak op GitHub een nieuwe repository, bijvoorbeeld `rijmaatje-android`.
2. Pak deze ZIP uit en upload de inhoud van de map naar de hoofdmap van de repository. Upload dus de bestanden en mappen zelf, niet alleen de ZIP.
3. Ga naar het tabblad **Actions** in je repository.
4. Kies **Build Rijmaatje APK** en tik op **Run workflow** (of wacht tot de workflow klaar is na een commit).
5. Open de geslaagde workflow-run, ga naar **Artifacts** en download `rijmaatje-debug-apk`.
6. Pak de artifact-ZIP uit en installeer `app-debug.apk` op je Samsung. Android kan vragen of je browser bestanden uit die bron mag installeren.

## Belangrijke volgende stap
Dit is een bouwbare basis, geen afgeronde versie voor scherm-uitgebruik. Daarna moeten we de native GPS-service koppelen aan de interface en de spraak/wake-word-functie apart implementeren en testen.
