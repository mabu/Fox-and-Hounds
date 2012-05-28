set title "Experiment 5, Graph 4: Untrained Learning Players"
set xlabel "Training steps (thousands)"
set ylabel "Wins"
set border 3
set xtics nomirror
set ytics nomirror
set terminal epslatex
set output "exp5graph4.eps"
set key bmargin
set xrange [0:100]
plot "exp5-3.csv" using 1:2 with lines \
    title 'Fox $\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.99$', \
    "exp5-3.csv" using 1:3 with lines \
    title 'Hounds $\epsilon = 0.2$, $\eta = 0.5$, $\gamma = 0.99$'
