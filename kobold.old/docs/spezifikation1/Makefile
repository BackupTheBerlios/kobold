###############################################################################
## Makefile for generating papers
## $Id: Makefile,v 1.2 2004/02/16 14:36:50 memyselfandi Exp $

TEXCMD=pdflatex
PDFCMD=pdflatex
PSCMD=dvips
PSCMDARGS=-o
INPUT=spezifikation1
TEXEXT=.tex
DVIEXT=.dvi
PSEXT=.ps

start: dvi

clean:
	rm -f *.pdf *.ps *.dvi *.toc *.aux *.log

all: clean dvi pub end

pub:
	$(PSCMD) $(PSCMDARGS) $(INPUT)$(PSEXT) $(INPUT)$(DVIEXT)
	$(PDFCMD) $(INPUT)$(TEXEXT)

dvi:
	$(TEXCMD) $(INPUT)$(TEXEXT)

end:
	acroread spezifikatin1.pdf
