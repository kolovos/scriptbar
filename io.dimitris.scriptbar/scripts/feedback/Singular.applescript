tell application "Skim"
	activate

	if (length of (get selection of document 1)) is 0 then
		return
	end if

	tell document 1
		set selectedText to ((get text for (get selection)) as string)

		if (selectedText is equal to "are") then
			set singular to "is"
		else
			set singular to ((characters 1 thru -2 of selectedText) as string)
		end if
		set noteText to singular
		make note with properties {type:highlight note, selection:selection, text:noteText}
	end tell

end tell
