import kivy
import calendar
import datetime
from kivy.app import App
from kivy.uix.label import Label
from kivy.uix.floatlayout import FloatLayout
from kivy.uix.button import Button
from kivy.graphics import Color, Ellipse
from kivy.uix.screenmanager import ScreenManager, Screen
from time_selection import time_screen

kivy.require('2.0.0')

class HomepageLayout(FloatLayout):
    def __init__(self):
        super(HomepageLayout,self).__init__()
        self.today = datetime.datetime.now()
        self.year = self.today.year
        self.month = self.today.month
        self.date = int(self.today.strftime("%d"))
        self.dis_month()

        self.sel_date = None
    def prev_month(self, instance):
        self.clear_widgets()
        if(self.month == 1):
            self.month = 12
            self.year-=1
        else:
            self.month-=1
        self.dis_month()
    def next_month(self, instance):
        self.clear_widgets()
        if(self.month == 12):
            self.month = 1
            self.year+=1
        else:
            self.month+=1
        self.dis_month()
    def basic(self):
        x = 1/7
        y = 1
        i = 2
        for day in ['Mon', 'Tues', 'Wed', 'Thurs', 'Fri', 'Sat']:
            self.add_widget(Label(text = day, size_hint = (1/7, 1/8), pos_hint = {'top': y, 'right':x}, color = (0,0,0,1)))
            x = i* 1/7
            i+=1
        self.add_widget(Label(text = 'Sun', size_hint = (1/7, 1/8), pos_hint = {'top': y, 'right':x}, color = (1,0.2,0.2,1)))
        self.add_widget(Label(text = str(self.month) + '/' + str(self.year), size_hint = (3/7, 1/8), pos_hint = {'bottom': 1, 'right': 5/7}, color = (1,1,1,1)))
        prev = Button(text = '<', pos_hint = {'bottom': 1, 'left': 1}, size_hint = (2/7, 1/8), background_color = (0, 0, 0, 1), background_normal = '')
        next = Button(text = '>', pos_hint = {'bottom': 1, 'right': 1}, size_hint = (2/7, 1/8), background_color = (0, 0, 0, 1), background_normal = '')
        self.add_widget(prev)
        self.add_widget(next)
        prev.bind(on_press = self.prev_month)
        next.bind(on_press = self.next_month)

    def day_press(self, instance):
        self.parent.manager.current = 'screen 2'
        self.sel_date = instance.text
        

    def dis_month(self):
        self.basic()
        cal_first_day = datetime.datetime(self.year, self.month, 1)
        m = int(cal_first_day.weekday())
        last_day = calendar.monthrange(self.year, self.month)[1]
        y = 1 - (1/8)
        for i in range(1, last_day+1):
            x = (m+1)*1/7
            if (i == self.date and  self.month == self.today.month and self.year == self.today.year):
                b = today_button(text = str(i), background_color = (0, 0, 0, 0), background_normal = '', size_hint = (1/7, 1/8), pos_hint = {'top': y, 'right' : x})
                with b.canvas:
                    Color(0,1,0,1)
                self.add_widget(b)
                b.bind(on_press = self.day_press)
            else:
                b = day_button(text = str(i), background_color = (0, 0, 0, 0), background_normal = '', size_hint = (1/7, 1/8), pos_hint = {'top': y, 'right' : x})
                self.add_widget(b)
                b.bind(on_press = self.day_press)
            m = (m+1) % 7
            if(m == 0):
                y = y - 1/8
    def date_selection(self, instance):
        self.sel_date = int(instance.text)
        self.ids['selected'].text = self.sel_date + '/' + self.month + '/' + self.year
        


class day_button (Button):
    def __init__(self, **kwargs):
        super(day_button, self).__init__(**kwargs)

        
class today_button (Button):
    def __init__(self, **kwargs):
        super(today_button, self).__init__(**kwargs)


class homescreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        layout = HomepageLayout()
        self.add_widget(layout)


class ForgetUsNot(App):
    def build(self):
        manager = ScreenManager()
        manager.add_widget(homescreen(name = 'screen 1'))
        manager.add_widget(time_screen(name = 'screen 2'))
        manager.current = 'screen 1'
        return manager
app = ForgetUsNot()
app.run()