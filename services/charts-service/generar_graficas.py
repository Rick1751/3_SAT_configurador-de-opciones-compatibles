"""Genera las gráficas del proyecto desde CSV producidos por Java."""
from pathlib import Path
import pandas as pd
import matplotlib.pyplot as plt

ROOT = Path(__file__).resolve().parents[2]
DATOS = ROOT / "data" / "resultados"
SALIDA = ROOT / "graficas"
SALIDA.mkdir(exist_ok=True)


def cargar(nombre, columnas):
    archivo = DATOS / nombre
    if not archivo.exists():
        print(f"Falta {nombre}: Ejecute primero la opción 11")
        return None
    datos = pd.read_csv(archivo)
    faltantes = [columna for columna in columnas if columna not in datos.columns]
    if faltantes:
        print(f"{nombre} no contiene columnas: {', '.join(faltantes)}")
        return None
    return datos


def guardar(nombre):
    plt.tight_layout()
    plt.savefig(SALIDA / nombre, dpi=160)
    plt.close()


def grafica_tamano(resumen):
    fig, ejes = plt.subplots(1, 3, figsize=(15, 5), sharey=True)
    for eje, familia in zip(ejes, resumen["familia"].unique()):
        parte = resumen[resumen["familia"] == familia]
        for algoritmo, grupo in parte.groupby("algoritmo"):
            eje.errorbar(grupo["n"], grupo["mediana_ms"],
                         yerr=[grupo["mediana_ms"] - grupo["minimo_ms"],
                               grupo["maximo_ms"] - grupo["mediana_ms"]],
                         marker="o", capsize=3, label=algoritmo)
        fuerza = parte[parte["algoritmo"] == "fuerza_bruta"].sort_values("n")
        if not fuerza.empty:
            base = fuerza.iloc[0]
            teorica = base["mediana_ms"] * (2 ** (fuerza["n"] - base["n"]))
            eje.plot(fuerza["n"], teorica, "--", label="c·2^n")
        eje.set_title(familia)
        eje.set_xlabel("Variables (n)")
        eje.grid(alpha=.25)
        eje.set_yscale("log")
    ejes[0].set_ylabel("Mediana de tiempo (ms, log)")
    ejes[-1].legend(fontsize=8)
    guardar("01_tamano_vs_tiempo.png")


def grafica_comparacion(resumen):
    fig, ejes = plt.subplots(1, 3, figsize=(15, 5), sharey=True)
    for eje, familia in zip(ejes, resumen["familia"].unique()):
        parte = resumen[resumen["familia"] == familia]
        pivote = parte.pivot(index="n", columns="algoritmo", values="mediana_ms")
        pivote.plot.bar(ax=eje, logy=True, title=familia)
        eje.set_xlabel("Variables (n)")
        eje.set_ylabel("Mediana (ms, log)")
        eje.grid(axis="y", alpha=.25)
    guardar("02_comparacion_algoritmos.png")


def grafica_encontrar(datos):
    plt.figure()
    for familia, grupo in datos.groupby("familia"):
        plt.plot(grupo["n"], grupo["tiempo_encontrar_fb_ns"], "o-", label=f"FB {familia}")
        plt.plot(grupo["n"], grupo["tiempo_encontrar_bt_ns"], "s-", label=f"BT {familia}")
    plt.plot(datos["n"], datos["tiempo_verificar_ns"], "^-", label="Verificar")
    plt.yscale("log")
    plt.xlabel("Variables (n)")
    plt.ylabel("Tiempo (ns, log)")
    plt.title("Encontrar frente a verificar")
    plt.grid(alpha=.25)
    plt.legend()
    guardar("03_encontrar_vs_verificar.png")


def grafica_exploracion(crudas):
    plt.figure()
    for clave, grupo in crudas.groupby(["algoritmo", "familia"]):
        grupo = grupo.groupby("n", as_index=False)["nodos_visitados"].median()
        plt.plot(grupo["n"], grupo["nodos_visitados"], "o-", label=" / ".join(clave))
    ns = sorted(crudas["n"].unique())
    base = ns[0]
    plt.plot(ns, [2 ** (n - base) for n in ns], "--", label="2^n normalizada")
    plt.plot(ns, [2 ** max(0, n - base - 3) for n in ns], ":", label="2^(n-3)")
    plt.yscale("log")
    plt.xlabel("Variables (n)")
    plt.ylabel("Nodos/asignaciones (log)")
    plt.title("Espacio explorado")
    plt.grid(alpha=.25)
    plt.legend(fontsize=8)
    guardar("04_asignaciones_exploradas.png")


def grafica_memoria(crudas):
    fig, ejes = plt.subplots(1, 2, figsize=(12, 5))
    agrupado = crudas.groupby(["algoritmo", "familia", "n"], as_index=False).median(numeric_only=True)
    for clave, grupo in agrupado.groupby(["algoritmo", "familia"]):
        etiqueta = " / ".join(clave)
        ejes[0].plot(grupo["n"], grupo["bytes_asignados"], "o-", label=etiqueta)
        ejes[1].plot(grupo["n"], grupo["profundidad_maxima"], "o-", label=etiqueta)
    ejes[0].set_title("Bytes asignados por hilo")
    ejes[0].set_ylabel("Bytes")
    ejes[1].set_title("Profundidad máxima")
    for eje in ejes:
        eje.set_xlabel("Variables (n)")
        eje.grid(alpha=.25)
        eje.legend(fontsize=7)
    guardar("05_memoria.png")


def grafica_dos_tres(datos, escalabilidad):
    plt.figure()
    for clave, grupo in datos.groupby("algoritmo"):
        plt.plot(grupo["n"], grupo["tiempo_ns"], "o-", label=clave)
    plt.yscale("log")
    plt.xlabel("Variables (n)")
    plt.ylabel("Tiempo (ns, log)")
    plt.title("2-SAT frente a 3-SAT con el mismo n")
    plt.grid(alpha=.25)
    plt.legend()
    guardar("06_2sat_vs_3sat.png")
    plt.figure()
    plt.plot(escalabilidad["n"], escalabilidad["tiempo_ns"], "o-", label="2-SAT")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("Variables (n, log)")
    plt.ylabel("Tiempo (ns, log)")
    plt.title("Escalabilidad de 2-SAT")
    plt.grid(alpha=.25)
    plt.legend()
    guardar("06b_2sat_escalabilidad.png")


def grafica_alpha(datos):
    fig, eje = plt.subplots()
    derecho = eje.twinx()
    eje.plot(datos["alpha"], datos["mediana_nodos"], "o-", label="Nodos")
    derecho.plot(datos["alpha"], datos["fraccion_sat"], "s--", color="tab:orange", label="Fracción SAT")
    eje.axvline(4.26, linestyle=":", color="black", label="α=4.26")
    eje.set_yscale("log")
    eje.set_xlabel("α = m/n")
    eje.set_ylabel("Mediana de nodos (log)")
    derecho.set_ylabel("Fracción SAT")
    eje.set_title("Umbral de dificultad de 3-SAT")
    eje.grid(alpha=.25)
    guardar("07_alpha_umbral.png")


def main():
    resumen = cargar("resumen_tiempo.csv",
                     ["algoritmo", "familia", "n", "mediana_ms", "minimo_ms", "maximo_ms"])
    crudas = cargar("resultados_tiempo.csv",
                    ["algoritmo", "familia", "n", "nodos_visitados", "bytes_asignados",
                     "profundidad_maxima"])
    encontrar = cargar("resultados_encontrar_vs_verificar.csv",
                       ["n", "tiempo_encontrar_fb_ns", "tiempo_encontrar_bt_ns",
                        "tiempo_verificar_ns"])
    dos_tres = cargar("resultados_2sat_vs_3sat.csv", ["n", "algoritmo", "tiempo_ns"])
    escala = cargar("resultados_2sat_escalabilidad.csv", ["n", "tiempo_ns"])
    alpha = cargar("resultados_alpha.csv", ["alpha", "mediana_nodos", "fraccion_sat"])
    if resumen is not None and crudas is not None:
        grafica_tamano(resumen)
        grafica_comparacion(resumen)
        grafica_exploracion(crudas)
        grafica_memoria(crudas)
    if encontrar is not None:
        grafica_encontrar(encontrar)
    if dos_tres is not None and escala is not None:
        grafica_dos_tres(dos_tres, escala)
    if alpha is not None:
        grafica_alpha(alpha)
    print(f"Gráficas generadas en {SALIDA}")


if __name__ == "__main__":
    main()
