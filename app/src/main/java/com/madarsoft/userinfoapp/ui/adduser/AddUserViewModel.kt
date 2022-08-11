package com.madarsoft.userinfoapp.ui.adduser

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madarsoft.userinfoapp.data.User
import com.madarsoft.userinfoapp.data.UserDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddUserViewModel @ViewModelInject constructor(
    private val userDao: UserDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val user = state.get<User>("user")

    private val addUserEventChannel = Channel<AddUserEvent>()
    val addUserEventFlow = addUserEventChannel.receiveAsFlow()

    var userName = (state.get<User>("userName") ?: user?.name ?: "") as String
        set(value) {
            field = value
            state.set("userName", value)
        }

    var userAge = (state.get<User>("userAge") ?: user?.age ?: "") as String
        set(value) {
            field = value
            state.set("userAge", value)
        }

    var userJobTitle = (state.get<User>("userJobTitle") ?: user?.jobTitle ?: "") as String
        set(value) {
            field = value
            state.set("userJobTitle", value)
        }

    var userGender = (state.get<User>("userGender") ?: user?.gender ?: "") as String
        set(value) {
            field = value
            state.set("userGender", value)
        }

    fun saveData() {
        var invalidInputMsg = ""

        if (userName.isEmpty())
            invalidInputMsg = "Name cannot by empty"
        else if (userAge.isEmpty())
            invalidInputMsg = "Age cannot by empty"
        else if (userJobTitle.isEmpty())
            invalidInputMsg = "Job title cannot by empty"
        else if (userGender.isEmpty())
            invalidInputMsg = "Select gender"

        if (invalidInputMsg != "")
            showInvalidInputMessage(invalidInputMsg)
        else
            createUser()
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addUserEventChannel.send(AddUserEvent.ShowInvalidInputMessage(text))
    }

    private fun createUser() = viewModelScope.launch {
        val newUser = User(userName, userAge, userJobTitle, userGender)

        userDao.insert(newUser)

        addUserEventChannel.send(AddUserEvent.UpdateUIOnUserInserted(newUser))
    }

    sealed class AddUserEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddUserEvent()
        data class UpdateUIOnUserInserted(val user: User) : AddUserEvent()
    }
}