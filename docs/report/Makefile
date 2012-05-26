GRAPHS=exp1.eps \
       exp2graph1.eps \
       exp2graph2.eps \
       exp3graph1.eps \
       exp3graph2.eps \
       exp4graph1.eps \
       exp4graph2.eps \
       exp4graph3.eps \
       exp4graph4.eps \

%.eps: %.gnuplot
	gnuplot $*.gnuplot
	epspdf $*.eps

report.pdf: $(GRAPHS) report.tex
	pdflatex report.tex

clean:
	rm -f *.pdf *.eps exp*.tex *.aux *.log
