package ru.unit.barsdiary.sdk.exception

class FinishRegistrationAccountException(val site: String) : Exception("Finish registering your account on the website ($site)")