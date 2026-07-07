const gkm = require("gkm");
const WebSocket = require("ws");

const WS_URL = "ws://127.0.0.1:5810";

const COMMANDS = {
    "i": { action: "press", table: "StreamDeck/IntakeRoller", key: "intakeToggle"  },
    "o": { action: "press", table: "StreamDeck/IntakeRoller", key: "outtakeToggle" },
    "a": { action: "press", table: "StreamDeck/IntakeAngle",  key: "toggleCount"   },
    "z": { action: "press", table: "StreamDeck/IntakeAngle",  key: "calibrateZero" },
    "t": { action: "press", table: "StreamDeck/Shooter",      key: "shooterToggle" },
};

const HOLD_COMMANDS = {
    "u": { table: "StreamDeck/Climb", key: "climbUp"   },
    "d": { table: "StreamDeck/Climb", key: "climbDown" },
    "y": { table: "StreamDeck/IntakeAngle", key: "manualPlus" },
    "p": { table: "StreamDeck/IntakeAngle", key: "manualMinus"},
};

let ws = null;
let reconnectAttempts = 0;
const MAX_RECONNECT = 10;
const keysDown = new Set();
const holdIntervals = {};

console.log("🎮 StreamDeck Global Listener iniciado");
console.log("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
console.log("  I → Intake ON   | O → Outtake");
console.log("  A → Angle Toggle| Z → Zero");
console.log("  T → Shooter     | Y → Manual+");
console.log("  U → Climb Up    | D → Climb Down");
console.log("  Q → Sair");
console.log("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

function connect() {
    console.log("🔗 Conectando ao bridge...");
    ws = new WebSocket(WS_URL);

    ws.on("open", () => {
        reconnectAttempts = 0;
        console.log("✅ Conectado! Aguardando teclas...");
    });

    ws.on("close", () => {
        console.log("🔌 Desconectado.");
        if (reconnectAttempts < MAX_RECONNECT) {
            reconnectAttempts++;
            setTimeout(connect, 3000);
        }
    });

    ws.on("error", (e) => console.error("⚠️ Erro:", e.message));
}

function send(payload) {
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        console.error("❌ WebSocket não conectado!");
        return;
    }
    ws.send(JSON.stringify(payload));
    console.log(`📤 ${payload.table}/${payload.key} = ${payload.value ?? "press"}`);
}

function startHold(key, table, ntKey) {
    // manda true imediatamente
    send({ action: "put", table, key: ntKey, value: true });

    // continua mandando true a cada 50ms enquanto segurar
    holdIntervals[key] = setInterval(() => {
        send({ action: "put", table, key: ntKey, value: true });
    }, 50);
}

function stopHold(key, table, ntKey) {
    if (holdIntervals[key]) {
        clearInterval(holdIntervals[key]);
        delete holdIntervals[key];
    }
    // manda false ao soltar
    send({ action: "put", table, key: ntKey, value: false });
}

gkm.events.on("key.pressed", (data) => {
    const key = data[0].toLowerCase();

    if (key === "q") {
        console.log("👋 Encerrando...");
        if (ws) ws.close();
        process.exit(0);
    }

    // evita key repeat
    if (keysDown.has(key)) return;
    keysDown.add(key);

    // tecla hold (climb)
    if (HOLD_COMMANDS[key]) {
        const { table, key: ntKey } = HOLD_COMMANDS[key];
        startHold(key, table, ntKey);
        return;
    }

    // tecla normal
    const cmd = COMMANDS[key];
    if (cmd) send(cmd);
});

gkm.events.on("key.released", (data) => {
    const key = data[0].toLowerCase();
    keysDown.delete(key);

    // para o hold ao soltar
    if (HOLD_COMMANDS[key]) {
        const { table, key: ntKey } = HOLD_COMMANDS[key];
        stopHold(key, table, ntKey);
    }
});

connect();