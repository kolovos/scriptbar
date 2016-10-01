# Exports a PDF with embedded notes that contains only the commented pages, next to the original PDF
tell application "Skim"
	activate
	set firstpage to 1
	set lastPage to count pages of document 1
	
	# Collect all numbers of pages with notes
	
	set pagesWithNotes to {}
	
	repeat with currentPage from firstpage to lastPage
		set pageNotes to notes of page currentPage of document 1
		
		repeat with pageNote in pageNotes
			if currentPage is not in pagesWithNotes then set end of pagesWithNotes to currentPage
		end repeat
		
	end repeat
	
	
	set filename to file of document 1
	set posixFileName to POSIX path of filename
	set exportedFileName to posixFileName & ".exported.pdf"
	#set redactedFileName to posixFileName & ".redacted.pdf"
	
	tell application "Finder" to set containerFolder to (get container of (filename as alias))
	set redactedFileName to POSIX path of ((choose file name with prompt "Save as file" default name "exported.pdf" default location (containerFolder as alias)))
	
	save document 1 as "PDF With Embedded Notes" in exportedFileName
	
	# Extract only pages with notes
	# Requires PDFTK from https://www.pdflabs.com/tools/pdftk-server/
	# Example: pdftk exp.pdf cat 1 2 3 output red.pdf
	
	set pathInit to "PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/usr/X11/bin; "
	set pdfTkScript to pathInit & "pdftk " & quote & exportedFileName & quote & " cat "
	repeat with pageNumber in pagesWithNotes
		set pdfTkScript to pdfTkScript & pageNumber & " "
	end repeat
	set pdfTkScript to pdfTkScript & " output " & quote & redactedFileName & quote
	
	log pdfTkScript
	
	do shell script pdfTkScript
	
	# Delete intermediate exported file
	
	set rmScript to pathInit & " rm " & quote & exportedFileName & quote
	do shell script rmScript
	
end tell