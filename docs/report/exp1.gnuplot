set title "Experiment 1: Graph 1"
set xlabel "Training steps (thousands)"
set ylabel "Wins"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp1.eps"
set key right center
plot "exp1.csv" using 1:2 with lines title 'Fox', \
    "exp1.csv" using 1:3 with lines title 'Hounds'
