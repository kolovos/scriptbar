tell application "Skim"
	activate
	
	if (length of (get selection of document 1)) is 0 then
		return
	end if
	
	tell document 1
		set selectedText to get selection as string
		make note with properties {type:highlight note, selection:selection, text:selectedText, color:deep pink}
	end tell
	
end tell
