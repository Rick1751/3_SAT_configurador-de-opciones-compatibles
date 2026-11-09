# Tablas generadas del benchmark

Estas tablas se regeneran con `python services/charts-service/generar_tablas_md.py`.

## Prueba | Tamaño | Tiempo | Memoria | Resultado

| Prueba | Familia | Tamaño de entrada | Tiempo de ejecución (ms) | Memoria medida (bytes) | Resultado |
| --- | --- | --- | --- | --- | --- |
| backtracking | aleatoria_4_26 | 12 | 0.125258 | 29544 | SAT |
| backtracking | aleatoria_4_26 | 14 | 0.079996 | 33648 | MIXTO 4/5 |
| backtracking | aleatoria_4_26 | 16 | 0.132631 | 37872 | SAT |
| backtracking | aleatoria_4_26 | 18 | 0.712667 | 41664 | MIXTO 3/5 |
| backtracking | aleatoria_4_26 | 20 | 0.594307 | 45544 | MIXTO 3/5 |
| backtracking | aleatoria_4_26 | 22 | 1.174534 | 50136 | SAT |
| backtracking | sat_plantada | 12 | 0.012525 | 27728 | SAT |
| backtracking | sat_plantada | 14 | 0.014984 | 32576 | SAT |
| backtracking | sat_plantada | 16 | 0.028516 | 36656 | SAT |
| backtracking | sat_plantada | 18 | 0.137129 | 41640 | SAT |

## Resumen por algoritmo y familia

| algoritmo | familia | n | mediana_ms | promedio_ms | desviacion_ms | minimo_ms | maximo_ms | mediana_bytes_asignados | mediana_nodos_o_asignaciones | resultado |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| backtracking | aleatoria_4_26 | 12 | 0.125258 | 0.131159 | 0.041743 | 0.076026 | 0.193811 | 29544 | 115 | SAT |
| backtracking | aleatoria_4_26 | 14 | 0.079996 | 0.078562 | 0.022003 | 0.050184 | 0.106523 | 33648 | 267 | MIXTO 4/5 |
| backtracking | aleatoria_4_26 | 16 | 0.132631 | 0.212651 | 0.213727 | 0.055094 | 0.629518 | 37872 | 482 | SAT |
| backtracking | aleatoria_4_26 | 18 | 0.712667 | 0.722735 | 0.422732 | 0.124822 | 1.192037 | 41664 | 1640 | MIXTO 3/5 |
| backtracking | aleatoria_4_26 | 20 | 0.594307 | 1.033797 | 0.979151 | 0.01473 | 2.539261 | 45544 | 1007 | MIXTO 3/5 |
| backtracking | aleatoria_4_26 | 22 | 1.174534 | 1.862801 | 1.469398 | 0.836057 | 4.7412 | 50136 | 2285 | SAT |
| backtracking | sat_plantada | 12 | 0.012525 | 0.014917 | 0.008356 | 0.006721 | 0.030321 | 27728 | 76 | SAT |
| backtracking | sat_plantada | 14 | 0.014984 | 0.016723 | 0.008323 | 0.007318 | 0.031734 | 32576 | 58 | SAT |
| backtracking | sat_plantada | 16 | 0.028516 | 0.030642 | 0.011835 | 0.019097 | 0.051823 | 36656 | 154 | SAT |
| backtracking | sat_plantada | 18 | 0.137129 | 0.256411 | 0.243876 | 0.018915 | 0.717059 | 41640 | 389 | SAT |
| backtracking | sat_plantada | 20 | 0.118143 | 0.467588 | 0.684618 | 0.014887 | 1.821399 | 45432 | 341 | SAT |
| backtracking | sat_plantada | 22 | 1.343536 | 2.398397 | 2.674855 | 0.514461 | 7.65766 | 50136 | 2478 | SAT |
| backtracking | unsat_peor_caso | 12 | 0.067044 | 0.083723 | 0.046832 | 0.029895 | 0.147376 | 27408 | 489 | UNSAT |
| backtracking | unsat_peor_caso | 14 | 0.306499 | 0.346035 | 0.203556 | 0.118111 | 0.705571 | 32400 | 923 | UNSAT |
| backtracking | unsat_peor_caso | 16 | 0.7356 | 0.805194 | 0.394784 | 0.344121 | 1.467896 | 36560 | 1873 | UNSAT |
| backtracking | unsat_peor_caso | 18 | 2.107142 | 2.025666 | 1.31751 | 0.563857 | 4.085718 | 41280 | 4423 | UNSAT |
| backtracking | unsat_peor_caso | 20 | 4.457884 | 6.176361 | 2.954139 | 3.165819 | 11.375716 | 45320 | 6989 | UNSAT |
| backtracking | unsat_peor_caso | 22 | 12.210504 | 12.750419 | 6.099152 | 5.030669 | 23.701455 | 49880 | 23681 | UNSAT |
| fuerza_bruta | aleatoria_4_26 | 12 | 0.186361 | 0.151002 | 0.072653 | 0.064305 | 0.245388 | 28296 | 2201 | SAT |
| fuerza_bruta | aleatoria_4_26 | 14 | 0.483155 | 0.505731 | 0.260131 | 0.11795 | 0.826147 | 33552 | 10279 | MIXTO 4/5 |
| fuerza_bruta | aleatoria_4_26 | 16 | 0.831572 | 1.071928 | 0.760614 | 0.291493 | 2.290368 | 37776 | 15542 | SAT |
| fuerza_bruta | aleatoria_4_26 | 18 | 2.689037 | 5.370177 | 4.536637 | 1.053991 | 11.598044 | 41384 | 41488 | MIXTO 3/5 |
| fuerza_bruta | aleatoria_4_26 | 20 | 27.290769 | 24.213153 | 14.143458 | 3.622268 | 44.31735 | 45448 | 633222 | MIXTO 3/5 |
| fuerza_bruta | aleatoria_4_26 | 22 | 46.754161 | 45.854146 | 24.640229 | 1.926654 | 73.856311 | 50024 | 1166182 | SAT |
| fuerza_bruta | sat_plantada | 12 | 0.024939 | 0.021911 | 0.011378 | 0.007563 | 0.03749 | 27648 | 498 | SAT |
| fuerza_bruta | sat_plantada | 14 | 0.256696 | 0.309746 | 0.123639 | 0.1499 | 0.471213 | 32480 | 7013 | SAT |
| fuerza_bruta | sat_plantada | 16 | 1.352271 | 1.577813 | 0.52432 | 0.970011 | 2.452136 | 36560 | 36368 | SAT |
| fuerza_bruta | sat_plantada | 18 | 0.825438 | 1.501642 | 1.504579 | 0.028513 | 3.663091 | 41544 | 12351 | SAT |
| fuerza_bruta | sat_plantada | 20 | 7.985637 | 8.242131 | 4.386281 | 2.641665 | 15.022347 | 45336 | 168193 | SAT |
| fuerza_bruta | sat_plantada | 22 | 16.443885 | 39.265167 | 37.750509 | 11.64834 | 111.00569 | 50024 | 393718 | SAT |
| fuerza_bruta | unsat_peor_caso | 12 | 0.096837 | 0.097343 | 0.001231 | 0.096547 | 0.099795 | 27328 | 4096 | UNSAT |
| fuerza_bruta | unsat_peor_caso | 14 | 0.410949 | 0.406179 | 0.012943 | 0.386178 | 0.424234 | 32304 | 16384 | UNSAT |
| fuerza_bruta | unsat_peor_caso | 16 | 1.796919 | 1.796683 | 0.038918 | 1.735536 | 1.858497 | 36464 | 65536 | UNSAT |
| fuerza_bruta | unsat_peor_caso | 18 | 6.709655 | 6.852525 | 0.203447 | 6.70128 | 7.222443 | 41184 | 262144 | UNSAT |
| fuerza_bruta | unsat_peor_caso | 20 | 31.631849 | 34.323626 | 11.13577 | 26.040124 | 56.01555 | 45224 | 1048576 | UNSAT |
| fuerza_bruta | unsat_peor_caso | 22 | 102.142244 | 102.546673 | 6.633281 | 94.81757 | 114.489725 | 49768 | 4194304 | UNSAT |
