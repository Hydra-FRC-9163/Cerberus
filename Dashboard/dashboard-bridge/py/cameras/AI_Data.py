import time

from cameras.RIO2WPILIB import (
    init_nt,
    rio2wpi_has_target,
)


# =========================
# CONFIGURACOES
# =========================

NT_SERVER = "10.91.63.2"
NT_TABLE = "limelight-back"

running = True


# =========================
# LOOP PRINCIPAL
# =========================

def main_loop():
    global running

    # Inicializa ou reutiliza a conexão NetworkTables.
    init_nt(
        server=NT_SERVER,
        table_name=NT_TABLE,
    )

    print("📷 Camera desativada.")
    print("📡 NetworkTables mantido ativo.")
    print("🎯 Nenhum alvo sera detectado por este modulo.")

    try:
        while running:
            # Informa que nao ha alvo detectado.
            rio2wpi_has_target(False)

            # Evita consumir CPU desnecessariamente.
            time.sleep(1.0)

    except KeyboardInterrupt:
        print("⏹ Encerrando AI_Data...")

    finally:
        rio2wpi_has_target(False)


# =========================
# EXECUCAO DIRETA
# =========================

if __name__ == "__main__":
    try:
        main_loop()
    except KeyboardInterrupt:
        running = False
        print("⏹ AI_Data encerrado.")