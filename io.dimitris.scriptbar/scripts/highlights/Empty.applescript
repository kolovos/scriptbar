tell application "Skim"
	activate
	
	if (length of (get selection of document 1)) is 0 then
		return
	end if
	
	tell document 1
		make note with properties {type:highlight note, selection:selection, color:white}
	end tell
	
end tell
