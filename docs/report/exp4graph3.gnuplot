set title "Experiment 4: Graph 3"
set xlabel "Training steps (100 thousand)"
set ylabel "Wins"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp4graph3.eps"
set key bmargin
plot "exp4-2.csv" using 1:2 with lines \
    title 'Fox $\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp4-2.csv" using 1:3 with lines \
    title 'Hounds $\epsilon = 0.1$, $\eta = 0.5$, $\gamma = 0.99$'
