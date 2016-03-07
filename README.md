# DooToo
GA First Project ToDo list app
3/5/2016

App Home Screen screenshot
http://i.imgur.com/FSvNGNe.png

This is a simple to-do list app with the following features:

Add new lists with user inputted titles.
Add new items to these lists by user input.
Edit the text values for either list names or items.
Delete lists or items. 


The app starts with screen having a field to enter text, a button that creates a list named by the entered text, and a list of all the existing list titles. Here users can create a new list (enter text, press "add" button), enter a list to see it contents (press on a list name), or edit the title of a list (long press on list name).

Entering a list switches screens to view the contents of that list. The list is titled on the top, a text field for user input and an "add" button are below, and the list contents are below. A new item can be added by entering text and then pressing the "add" button. Long pressing on an item allows the user to edit the item.

Editing either a list title or an item is done on a separate screen where the text to be edited is presented on top along with a field for input, and the context of the items in the list is presented below. Users type in their changes and then press one of three buttons, "accept" to accept the changes and return to the previous screen, "cancel" to make no changes and return to the previous screen, and "delete" to delete the item entirely. Deleting an item can also be done by pressing "accept" when the text field is empty. Upon deletion request, a message asking if the user really wants to delete and two buttons, "cancel," and "delete" appear. Cancel will simply allow the user to edit the field again, whereas as "delete" goes forward with deletion. In the case of editing a list title, deleting this will delete the entire list contents as well.



There are no known bugs (though the code has no null checks). One notable anti-pattern is that if a user enters the edit text screen, does nothing, and presses "accept," they are prompted to delete that object since the text field is empty form their inaction. This may promote unwanted deletions. "