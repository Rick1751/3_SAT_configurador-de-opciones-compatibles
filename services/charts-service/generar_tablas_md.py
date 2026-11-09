"""Genera tablas Markdown a partir de los CSV reales del benchmark."""
from pathlib import Path
import pandas as pd

ROOT = Path(__file__).resolve().parents[2]
DATOS = ROOT / "data" / "resultados"
DESTINO = ROOT / "docs" / "tablas_generadas.md"

def markdown(tabla):
    encabezado = "| " + " | ".join(str(c) for c in tabla.columns) + " |"
    separador = "| " + " | ".join("---" for _ in tabla.columns) + " |"
    filas = ["| " + " | ".join(str(v) for v in fila) + " |"
             for fila in tabla.itertuples(index=False, name=None)]
    return "\n".join([encabezado, separador, *filas])


def main():
    resumen = DATOS / "resumen_tiempo.csv"
    if not resumen.exists():
        print("Falta un CSV: Ejecute primero la opción 11")
        return
    datos = pd.read_csv(resumen)
    if datos.empty:
        print("El resumen no tiene filas.")
        return
    columnas = ["algoritmo", "familia", "n", "mediana_ms", "mediana_bytes_asignados",
                "resultado"]
    muestra = datos[columnas].head(10).rename(columns={
        "algoritmo": "Prueba", "familia": "Familia", "n": "Tamaño de entrada",
        "mediana_ms": "Tiempo de ejecución (ms)",
        "mediana_bytes_asignados": "Memoria medida (bytes)", "resultado": "Resultado"
    })
    DESTINO.write_text(
        "# Tablas generadas del benchmark\n\n"
        "Estas tablas se regeneran con `python services/charts-service/generar_tablas_md.py`.\n\n"
        "## Prueba | Tamaño | Tiempo | Memoria | Resultado\n\n"
        + markdown(muestra)
        + "\n\n## Resumen por algoritmo y familia\n\n"
        + markdown(datos)
        + "\n",
        encoding="utf-8",
    )
    print(f"Tabla escrita en {DESTINO}")


if __name__ == "__main__":
    main()
