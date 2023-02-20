import kivy
from kivy.uix.button import Button
from kivy.uix.floatlayout import FloatLayout
from kivy.uix.screenmanager import Screen
from kivy.uix.spinner import Spinner
from kivy.uix.textinput import TextInput


class TimeSelector(FloatLayout):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        
        self.hour_spinner = TSpinner(
            text='12', values=[str(i) for i in range(1, 13)], size_hint = (1/3, 1/2), pos_hint = {'top': 1, 'right': 1/3})
        
        self.minute_spinner = TSpinner(
            text='00', values=[str(i).zfill(2) for i in range(0, 60, 5)], size_hint = (1/3, 1/2), pos_hint = {'top': 1, 'right': 2/3})
        
        self.ampm_spinner = TSpinner(text='AM', values=['AM', 'PM'], size_hint = (1/3, 1/2), pos_hint = {'top': 1, 'right': 1})
        
        self.add_widget(self.hour_spinner)
        self.add_widget(self.minute_spinner)
        self.add_widget(self.ampm_spinner)

        self.t_input = TextInput(size_hint = (1, 1/2), text = 'Add Reminder Text here')
        self.add_widget(self.t_input)
        self.b = Button(text = 'Add Reminder', size_hint = (1/4, 1/8), pos_hint = {'bottom': 1, 'right': 1})
        self.b.bind(on_press = self.add_reminder)
        self.add_widget(self.b)
    def add_reminder(self, instance):
        print(self.t_input.text, self.hour_spinner.text, self.minute_spinner.text, self.ampm_spinner.text)
        return

class TSpinner(Spinner):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)


class time_screen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        layout = TimeSelector()
        self.add_widget(layout)



