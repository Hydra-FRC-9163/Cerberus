const gkm = require("gkm");
const { spawn } = require("child_process");
const path = require("path");

console.log("🎮 StreamDeck Global Listener iniciado");
console.log("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
console.log("I → Intake ON");
console.log("O → Outtake");
console.log("A → Angle Toggle");
console.log("Z → Zero");
console.log("T → Shooter");
console.log("U → Climb Up");
console.log("D → Climb Down");
console.log("Y → Intake Manual +");
console.log("P → Intake Manual -");
console.log("Q → Sair");
console.log("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

const script = path.join(__dirname, "send_sd_command.js");

const keysDown = new Set();

function send(command) {
    spawn("node", [script, command], { stdio: "inherit" });
}

gkm.events.on("key.pressed", (data) => {
    const key = data[0].toLowerCase();

    if (keysDown.has(key)) return;
    keysDown.add(key);

    switch (key) {
        case "i": send("INTAKE_ON");       break;
        case "o": send("INTAKE_OFF");      break;
        case "a": send("ANGLE_TOGGLE");    break;
        case "z": send("ANGLE_ZERO");      break;
        case "t": send("SHOOTER_ON");      break;
        case "u": send("CLIMB_UP");        break;
        case "d": send("CLIMB_DOWN");      break;
        case "y": send("INTAKE_MANUAL_P"); break;
        case "p": send("INTAKE_MANUAL_M"); break;
        case "q":
            console.log("👋 Encerrando...");
            process.exit(0);
    }
});

gkm.events.on("key.released", (data) => {
    const key = data[0].toLowerCase();
    keysDown.delete(key);
});