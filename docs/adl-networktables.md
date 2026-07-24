# ADL NetworkTables Contract

Robot code reads these intent topics:

- `/ADL/intent/actionId` string: modular action id such as `acquire_piece`, `score_piece`, `balance`, or `abort`.
- `/ADL/intent/zoneId` string: target zone id such as `pieces`, `grids`, `station`, or `unknown`.
- `/ADL/intent/parameters` string: optional semicolon-delimited `key=value` pairs, for example `pieceType=CONE`.
- `/ADL/intent/priority` double: optional request priority. `0` uses the action default.
- `/ADL/intent/preempt` boolean: whether this request may preempt the active action.
- `/ADL/intent/sequence` double: increment this for every new request, even when `actionId` is unchanged.
- `/ADL/intent` string: legacy compatibility input. Supported values are `ACQUIRE_PIECE`, `SCORE_PIECE`, `BALANCE`, and `ABORT`.

Robot code publishes these ADL topics:

- `/ADL/state` string: dashboard state derived from the active action, such as `ACQUIRING`, `SCORING`, `BALANCING`, or `EMERGENCY`.
- `/ADL/decision` string: last decision status and reason.
- `/ADL/activeAction` string: currently scheduled modular action id.
- `/ADL/dashboard/state` string: dashboard mirror of `/ADL/state`.
- `/ADL/dashboard/decision` string: dashboard mirror of `/ADL/decision`.
- `/ADL/dashboard/activeAction` string: dashboard mirror of `/ADL/activeAction`.
- `/ADL/dashboard/enabled` boolean: whether the modular ADL manager is active in `RobotContainer`.

The ADL context provider also reads robot facts from `/RobotStress/*`, `/Mechanisms/*`, `/Vision/*`, `/Drive/*`, `/Robot/*`, `/Game/Endgame`, and `/ChargeStation/*`.
