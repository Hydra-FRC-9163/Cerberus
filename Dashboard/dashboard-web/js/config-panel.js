"use strict";

import {
    ntSend,
    onNTMessage,
    onConnectionChange
} from "./ws.js";

// =========================
// ELEMENTOS DA DASHBOARD
// =========================

const status = document.getElementById("config-status");
const inputs = new Map();

// =========================
// CONFIGURACAO DOS BOTOES
// ======-===================

document.querySelectorAll("[data-config-table]").forEach((button) => {
    const table = button.dataset.configTable;
    const key = button.dataset.configKey;
    const inputId = button.dataset.configInput;
    const input = document.getElementById(inputId);

    if (!table || !key || !input) {
        console.warn(
            "[Config] Configuração inválida no botão:",
            button
        );
        return;
    }

    const topic = `/${table}/${key}`;

    inputs.set(topic, input);

    button.addEventListener("click", () => {
        const value = input.valueAsNumber;

        if (!Number.isFinite(value) || value < -1 || value > 1) {
            setStatus("Use um valor entre -1 e 1.");
            return;
        }

        ntSend({
            action: "put",
            table,
            key,
            value
        });

        setStatus(`Enviado: ${key} = ${value}`);
    });
});

// =========================
// RECEBER VALORES DO ROBOT
// =========================

onNTMessage((topic, value) => {
    const input = inputs.get(topic);

    if (!input) {
        return;
    }

    // Nao sobrescreve o campo enquanto o usuario estiver editando.
    if (document.activeElement !== input) {
        const number = Number(value);

        if (Number.isFinite(number)) {
            input.value = number;
        }
    }
});

// =========================
// STATUS DA CONEXAO
// =========================

onConnectionChange((online) => {
    if (online) {
        setStatus("Conectado à bridge WebSocket.");
    } else {
        setStatus("Desconectado — tentando reconectar...");
    }
});

// =========================
// AUXILIAR
// =========================

function setStatus(message) {
    if (status) {
        status.textContent = message;
    }
}

console.log("[Config] Painel de configuração carregado.");