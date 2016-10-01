tell application "Skim"
	activate

	if (length of (get selection of document 1)) is 0 then
		return
	end if

	tell document 1
		set selectedText to ((get text for (get selection)) as string)

		if (selectedText is equal to "is") then
			set replacement to "was"
		else if (selectedText is equal to "was") then
			set replacement to "is"
		else if (selectedText is equal to "are") then
			set replacement to "were"
		else if (selectedText is equal to "were") then
			set replacement to "are"			
		else
			return
		end if
		set noteText to replacement
		make note with properties {type:highlight note, selection:selection, text:noteText}
	end tell

end tell
