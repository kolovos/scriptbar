tell application "Skim"
	activate
	set filename to file of document 1
	set posixFileName to POSIX path of filename
	set textFileName to posixFileName & ".txt"
	set subl to quote & "/Applications/Sublime Text.app/Contents/SharedSupport/bin/subl" & quote & " " & quote & textFileName & quote
	do shell script subl
end tell