tell application "Skim"
	activate

	if (length of (get selection of document 1)) is 0 then
		return
	end if

	tell document 1
		set selectedText to get text for (get selection)
		set noteText to the text returned of (display dialog "Enter a comment" default answer selectedText & " -> " & selectedText)
		make note with properties {type:highlight note, selection:selection, text:noteText}
	end tell

end tell
