set title "Experiment 5, Graph 5: Untrained Learning Players"
set xlabel "Training steps (thousands)"
set ylabel "States visited"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp5graph5.eps"
set key bmargin
plot "exp5-0.csv" using 1:4 with lines \
    title 'Fox $\epsilon = 0.1$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp5-0.csv" using 1:5 with lines \
    title 'Hounds $\epsilon = 0.1$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp5-3.csv" using 1:4 with lines \
    title 'Fox $\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp5-3.csv" using 1:5 with lines \
    title 'Hounds $\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.99$'
