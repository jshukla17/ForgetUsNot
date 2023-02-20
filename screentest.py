from kivy.app import App
from kivy.uix.button import Button
from kivy.uix.screenmanager import ScreenManager, Screen

class Screen1(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.add_widget(Button(text='Go to Screen 2', on_press=self.go_to_screen2))

    def go_to_screen2(self, instance):
        self.manager.current = 'screen2'

class Screen2(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.add_widget(Button(text='Go to Screen 1', on_press=self.go_to_screen1))

    def go_to_screen1(self, instance):
        self.manager.current = 'screen1'

class MyApp(App):
    def build(self):
        screen_manager = ScreenManager()
        screen_manager.add_widget(Screen1(name='screen1'))
        screen_manager.add_widget(Screen2(name='screen2'))
        return screen_manager

MyApp().run()
