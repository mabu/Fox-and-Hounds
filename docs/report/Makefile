GRAPHS=exp1.eps \
       exp2graph1.eps \
       exp2graph2.eps \

%.eps: %.gnuplot
	gnuplot $*.gnuplot
	epspdf $*.eps

report.pdf: $(GRAPHS) report.tex
	pdflatex report.tex

clean:
	rm -f *.pdf *.eps exp*.tex *.aux *.log
