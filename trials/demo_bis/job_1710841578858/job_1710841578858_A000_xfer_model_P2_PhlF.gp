
set output "job_1710841578858_A000_xfer_model_P2_PhlF.eps"
set terminal postscript eps enhanced color "Helvetica, 35" size 2,2
set logscale x
set logscale y
set lmargin screen 0.0
set rmargin screen 1.0
set tmargin screen 1.0
set bmargin screen 0.0
set size ratio 1.0
set border linewidth 2
set tics scale 2
set mxtics 10
set mytics 10
set key bottom left
set key samplen -1
set xrange [0.001:1000.0]
set yrange [0.001:1000.0]
set format y "10^{%L}"    
set format x "10^{%L}"    
set format x ""    
set xlabel '00000001'
set arrow from 0.08274575867861855,0.001 to 0.08274575867861855,1000.0 nohead lw 10 lt 2 lc rgb '#000000'
set arrow from 0.8612168136232772,0.001 to 0.8612168136232772,1000.0 nohead lw 10 lt 2 lc rgb '#000000'
ymin = 0.02
ymax = 4.1
K = 0.13
n = 3.9
set dummy x

plot ymin+(ymax-ymin)/(1.0+(x/K)**n) lw 25 lc rgb '#F9A427' title 'P2PhlF',\
 "<echo '1 2'" using (0.130327211):(2.0500000033606627)  with points pt 7 ps 4 lc rgb 'black' notitle,\
 "<echo '1 2'" using (0.507702151):(0.03999999993820334)  with points pt 7 ps 4 lc rgb 'black' notitle