###############################################################################
## Makefile for generating papers
## $Id: Makefile,v 1.1 2004/05/20 12:36:16 garbeam Exp $

TEXCMD=pdflatex
PDFCMD=pdflatex
PSCMD=dvips
PSCMDARGS=-o
INPUT=usermanual
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
	acroread usermanual.pdf
