tell application "Skim"
	activate
	
	if (length of (get selection of document 1)) is 0 then
		return
	end if
	
	tell document 1
		set selectedText to ((get text for (get selection)) as string)
		make note with properties {type:highlight note, selection:selection, text:selectedText, color: violet}
	end tell
	
end tell
