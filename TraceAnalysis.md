# Analise des Services Wireshark Traces

## Allgemein
Das protokoll im algemeinen funktioniert über ein TCP verbindung
und implementiert bzw sendet die nötigen pakete um ein eben diese verbindung aufzubauen und zu erhalten.
Des weiteren werden Information mit dem Protokoll über in ascii codierten text im Data bereich übertragen.
Es ist basically eine HashMap über eine TCP verbindung
## User Message 
Im allgemeinen folgen die messages des users dem folgenden Format:

>   `COMMAND` `key` `[value]` `\r\n`

Die möglichen Commands und ihr verhalten kann aus der folgenden Tabelle entnommen werden:

| Command  | Arguments     | Description                                                      |
|----------|---------------|------------------------------------------------------------------|
| `GET`    | `key`         | Retrieves the value which was previously defined for a given key |
| `PUT`    | `key` `value` | Defines the value for the given key                              |
| `EXIT`   |               | User message to end the conversation with the server             |

## Server Messages
Der server/service Provider antworter nur auf die Anfragen des users und kann nicht selber request schicken.
Die antworten Folgen einem Ähnlichen schema wo oben:

> `RESP CODE` `RESP MSG` `\n`

Wobei der RESP CODE immer angibt ober der befehl successfull war oder einen Error geworfen hat.
In der RESP MSG wird dann genauer Spezifiert was schief ging und warum.
Allgemein Folgt es dieser Convetion:

TODO: Diese tabelle richting ausfüllen bzw umstruckturieren weil das ist komisch

| RESP CODE | RESP MSG                      | Description                              |
|-----------|-------------------------------|------------------------------------------|
| ERR:      | Unkown Key! & Unkown Command! |                                          |
| RES:      | Bye or Value or OK            | Responds with OK for valid put requests. |