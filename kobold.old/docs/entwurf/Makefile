###############################################################################
## Makefile for generating papers
## $Id: Makefile,v 1.1 2004/03/22 21:00:34 rendgeor Exp $

TEXCMD=pdflatex
PDFCMD=pdflatex
PSCMD=dvips
PSCMDARGS=-o
INPUT=entwurf
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
	acroread entwurf.pdf
