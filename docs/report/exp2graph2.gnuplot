set title "Experiment 2, Graph 2: Hounds vs Random Fox"
set xlabel "Training steps (100 thousand)"
set ylabel "Wins"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp2graph2.eps"
set key right center
plot "exp2-4.csv" using 1:3 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.3$, $\gamma = 0.99$', \
    "exp2-5.csv" using 1:3 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.3$, $\gamma = 0.99$', \
    "exp2-6.csv" using 1:3 with lines \
    title '$\epsilon = 0.1$, $\eta = 0.3$, $\gamma = 0.95$', \
    "exp2-7.csv" using 1:3 with lines \
    title '$\epsilon = 0.2$, $\eta = 0.3$, $\gamma = 0.95$'
