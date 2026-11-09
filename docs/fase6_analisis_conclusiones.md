# Fase 6: análisis y conclusiones

1. **¿Qué determina el tiempo?** Influyen n, m, α, orden de variables, poda,
   resultado SAT/UNSAT y calentamiento JIT. Los CSV separan `aleatoria_4_26`,
   `sat_plantada` y `unsat_peor_caso`.
2. **¿Qué pasa al aumentar n?** Fuerza bruta tiene hasta `2^n` máscaras y
   backtracking hasta `2^(n+1)-1` nodos. En `resultados_tiempo.csv` se pueden
   los promedios UNSAT peor caso fueron 99.342 µs (n=12), 1.347 ms (n=16),
   23.098 ms (n=20) y 90.699 ms (n=22). Las razones no son constantes por JIT
   y resolución del reloj.
3. **¿Coincide con teoría?** Cualitativamente sí: UNSAT peor caso crece
   exponencialmente, mientras que SAT puede terminar antes. Las desviaciones
   provienen de JIT, caché y poda; no se sustituyen por cifras inventadas.
4. **¿Encontrar o verificar?** Encontrar explora máscaras o nodos; verificar
   una asignación ya validada recorre cláusulas. En n=22, las razones
   encontrar/verificar fueron 9.571x (plantada), 956.722x (aleatoria) y
   30.569.768x (UNSAT peor caso), según el CSV.
5. **¿Limitaciones?** No incluye CDCL, heurísticas sofisticadas ni propagación
   unitaria; la fuerza bruta sigue limitada por el crecimiento exponencial.
6. **¿Mejoras?** DPLL, propagación unitaria, literales puros, selección
   heurística y CDCL reducirían nodos en casos reales.
7. **¿Uso real?** Configuradores de hardware/productos, planificación,
   compatibilidad de dependencias y verificación de reglas.

El barrido de α tuvo su máximo observado en α=4.00, con 201.063 nodos medianos
y fracción SAT 0.750 sobre 20 instancias. Es una muestra pequeña: el umbral
teórico aproximado 4.26 no obliga a que el máximo experimental coincida
exactamente.

La conclusión principal es que la representación simple permite observar
directamente la diferencia entre verificar un certificado y encontrarlo. Las
tablas y figuras se regeneran desde los CSV para mantener trazabilidad.
