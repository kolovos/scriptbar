tell application "Skim"
	activate
	
	if (length of (get selection of document 1)) is 0 then
		return
	end if
	
	tell document 1
		set selectedText to ((get text for (get selection)) as string)
		set noteText to the text returned of (display dialog "Why?" default answer "")
		make note with properties {type:strike out note, selection:selection, text:noteText}
	end tell
	
end tell
