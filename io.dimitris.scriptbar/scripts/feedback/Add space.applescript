# Adds an "Add space" comment
tell application "Skim"
	activate
	
	if (length of (get selection of document 1)) is 0 then
		return
	end if
	
	tell document 1
		set noteText to "Add space"
		make note with properties {type:highlight note, selection:selection, text:noteText}
	end tell
	
end tell
