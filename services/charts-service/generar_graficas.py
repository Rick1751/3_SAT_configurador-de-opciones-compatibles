"""Genera gráficas únicamente a partir de CSV producidos por el benchmark."""
from pathlib import Path
try:
    import pandas as pd
    import matplotlib.pyplot as plt
except ImportError as error:
    print("Faltan dependencias de gráficas. Instale con: python3 -m pip install -r "
          "services/charts-service/requirements.txt")
    raise SystemExit(1) from error

ROOT = Path(__file__).resolve().parents[2]
DATOS = ROOT / "data" / "resultados"
SALIDA = ROOT / "graficas"
SALIDA.mkdir(exist_ok=True)

def main():
    archivo = DATOS / "resultados_tiempo.csv"
    if not archivo.exists():
        print("Falta el CSV: ejecute primero la opción 11 del menú.")
        return
    df = pd.read_csv(archivo)
    if df.empty:
        print("El CSV no contiene datos para graficar.")
        return
    for nombre, columna, titulo, eje in [
        ("01_tamano_vs_tiempo.png", "tiempo_ns", "Tamaño vs tiempo", "Tiempo (ns)"),
        ("02_comparacion_algoritmos.png", "tiempo_ns", "Comparación de algoritmos", "Tiempo (ns)"),
        ("04_asignaciones_exploradas.png", "asignaciones_probadas", "Asignaciones exploradas", "Asignaciones"),
        ("05_memoria.png", "memoria_bytes", "Memoria estimada", "Bytes"),
    ]:
        plt.figure()
        for algoritmo, grupo in df.groupby("algoritmo"):
            plt.plot(grupo["n"], grupo[columna], "o-", label=algoritmo)
        plt.xlabel("Variables (n)"); plt.ylabel(eje); plt.title(titulo)
        if nombre in {"01_tamano_vs_tiempo.png", "02_comparacion_algoritmos.png"}:
            plt.yscale("log")
        plt.grid(alpha=.25); plt.legend(); plt.tight_layout()
        plt.savefig(SALIDA / nombre, dpi=160); plt.close()
    encontrar = DATOS / "resultados_encontrar_vs_verificar.csv"
    if encontrar.exists():
        ev = pd.read_csv(encontrar)
        plt.figure()
        plt.plot(ev["n"], ev["tiempo_resolver_ns"], "o-", label="Encontrar")
        plt.plot(ev["n"], ev["tiempo_verificar_ns"], "o-", label="Verificar")
        plt.xlabel("Variables (n)"); plt.ylabel("Tiempo (ns)"); plt.title("Encontrar vs verificar")
        plt.yscale("log"); plt.grid(alpha=.25); plt.legend(); plt.tight_layout()
        plt.savefig(SALIDA / "03_encontrar_vs_verificar.png", dpi=160); plt.close()
    comparacion = DATOS / "resultados_2sat_vs_3sat.csv"
    if comparacion.exists():
        dos = pd.read_csv(comparacion)
        plt.figure()
        for algoritmo, grupo in dos.groupby("algoritmo"):
            plt.plot(grupo["n"], grupo["tiempo_ns"], "o-", label=algoritmo)
        plt.xlabel("Variables (n)"); plt.ylabel("Tiempo (ns)"); plt.title("2-SAT vs 3-SAT")
        plt.yscale("log"); plt.grid(alpha=.25); plt.legend(); plt.tight_layout()
        plt.savefig(SALIDA / "06_2sat_vs_3sat.png", dpi=160); plt.close()
    alpha = DATOS / "resultados_alpha.csv"
    if alpha.exists():
        al = pd.read_csv(alpha)
        if not al.empty:
            plt.figure()
            plt.plot(al["alpha"], al["tiempo_ns"], "o-")
            plt.xlabel("alpha = m/n"); plt.ylabel("Tiempo (ns)"); plt.title("Dificultad según alpha")
            plt.grid(alpha=.25); plt.tight_layout()
            plt.savefig(SALIDA / "07_alpha_umbral.png", dpi=160); plt.close()
    print(f"Gráficas generadas en {SALIDA}")

if __name__ == "__main__":
    main()
