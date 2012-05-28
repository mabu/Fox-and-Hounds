set title "Experiment 2, Graph 1: Hounds vs Random Fox"
set xlabel "Training steps (100 thousand)"
set ylabel "Wins"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp2graph1.eps"
set key right center
plot "exp2-0.csv" using 1:3 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp2-1.csv" using 1:3 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp2-2.csv" using 1:3 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.5$, $\gamma = 0.95$', \
    "exp2-3.csv" using 1:3 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.95$'
