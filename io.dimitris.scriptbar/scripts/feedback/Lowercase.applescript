# Recommends that the first character of the selection is turned to lowercase
tell application "Skim"
	activate

	if (length of (get selection of document 1)) is 0 then
		return
	end if

	tell document 1
		set selectedText to get selection as string
		set firstToLowerCase to do shell script "echo " & character 1 of selectedText & " | tr [:upper:] [:lower:]"
		set firstToLowerCase to firstToLowerCase & characters 2 through -1 of selectedText
		set noteText to firstToLowerCase
		make note with properties {type:highlight note, selection:selection, text:noteText}
	end tell

end tell
