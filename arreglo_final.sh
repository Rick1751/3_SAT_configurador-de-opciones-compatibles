#!/usr/bin/env bash
# Arreglos de último minuto. Ejecutar desde la raíz del repo: ./arreglo_final.sh
set -e
echo "1) Restaurando CSV consistentes (el último commit dejó uno vacío)"
git checkout bd24093 -- data/resultados/

echo "2) Corrigiendo diagramas"
sed -i 's/Θ(sum |c|)/Θ(Σ tamaños)/g; s/O(sum |c|)/O(Σ tamaños)/g' diagramas/complejidad/complejidad_algoritmos.puml
sed -i '/^<font:monospaced>$/d; /^<\/font>$/d' diagramas/pseudocodigo/*.puml
for f in diagramas/pseudocodigo/*.puml; do
  grep -q "defaultFontName" "$f" || sed -i '0,/^@startuml/s//@startuml\nskinparam defaultFontName Monospaced/' "$f"
done

echo "3) Quitando la razón engañosa de UNSAT en la gráfica 03"
sed -i 's/^        eje.annotate(f"FB\/verificar/        if familia != "unsat_peor_caso": eje.annotate(f"FB\/verificar/' services/charts-service/generar_graficas.py

echo "4) Corrigiendo textos de docs"
python - <<'PY'
import re, pathlib
f = pathlib.Path("docs/fase6_analisis_conclusiones.md"); t = f.read_text(encoding="utf-8")
t = re.sub(r"En `resultados_tiempo\.csv` se pueden\s+los promedios UNSAT peor caso fueron.*?y resolución del reloj\.",
 "En `unsat_peor_caso`, las medianas de **fuerza bruta** fueron 0,096 ms (n=12), 1,339 ms (n=16),\n"
 "   21,72 ms (n=20) y 107,46 ms (n=22); las de **backtracking** fueron 0,221 ms, 3,164 ms, 50,22 ms y\n"
 "   264,74 ms. Cada +2 en n multiplica el tiempo por ≈4, como predice 2^n. Backtracking resulta más\n"
 "   lento que fuerza bruta en este peor caso: visita 2^(n+1)-1 nodos y cada nodo cuesta O(m), y sin\n"
 "   cláusulas de relleno la poda solo actúa en el último nivel.", t, flags=re.S)
t = re.sub(r"En n=22, las razones\s+encontrar/verificar fueron 9\.571x \(plantada\), 956\.722x \(aleatoria\) y\s+30\.569\.768x \(UNSAT peor caso\), según el CSV\.",
 "En n=22, las razones\n   encontrar/verificar fueron 9.571x (plantada) y 956,7x (aleatoria), según el CSV. En UNSAT no existe un\n"
 "   certificado corto de insatisfacibilidad (tema de co-NP), por lo que no se reporta esa razón.", t, flags=re.S)
f.write_text(t, encoding="utf-8")
v = pathlib.Path("docs/VERIFICACION.md"); s = v.read_text(encoding="utf-8")
s = s.replace("956.722x (aleatoria) y\n30.569.768x (UNSAT peor caso).", "956.722x (aleatoria). En UNSAT no hay certificado corto, por eso no se reporta razón.")
v.write_text(s, encoding="utf-8")
PY

echo "5) PDF con weasyprint"
sed -i 's/--pdf-engine=xelatex/--pdf-engine=weasyprint/' scripts/generar_informe_pdf.sh

echo "6) Regenerando gráficas y tablas"
python services/charts-service/generar_graficas.py
python services/charts-service/generar_tablas_md.py

echo "7) Diagramas: validar y dibujar"
plantuml -checkonly $(find diagramas -name '*.puml')
mkdir -p diagramas/render
plantuml -tpng -o ../render $(find diagramas -name '*.puml')

echo "8) PDF del informe"
./scripts/generar_informe_pdf.sh || echo "AVISO: PDF no generado"
echo "LISTO"
