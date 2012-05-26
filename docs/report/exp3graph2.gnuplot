set title "Experiment 4: Graph 2"
set xlabel "Training steps (100 thousand)"
set ylabel "Wins"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp3graph2.eps"
set key right center
plot "exp3-4.csv" using 1:2 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.3$, $\gamma = 0.99$', \
    "exp3-5.csv" using 1:2 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.3$, $\gamma = 0.99$', \
    "exp3-6.csv" using 1:2 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.3$, $\gamma = 0.95$', \
    "exp3-7.csv" using 1:2 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.3$, $\gamma = 0.95$'
