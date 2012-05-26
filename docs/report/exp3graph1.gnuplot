set title "Experiment 3: Graph 1"
set xlabel "Training steps (100 thousand)"
set ylabel "Wins"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp3graph1.eps"
set key right center
plot "exp3-0.csv" using 1:2 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp3-1.csv" using 1:2 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp3-2.csv" using 1:2 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.5$, $\gamma = 0.95$', \
    "exp3-3.csv" using 1:2 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.95$'
