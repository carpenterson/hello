#!/usr/bin/python
import string
import random
import time
import datetime

def get_random_id():
    alphabet = list(string.ascii_lowercase + string.digits)
    return ''.join([random.choice(alphabet) for _ in range(32)])

start=datetime.datetime.strptime("2021-01-15 10:29:00", "%Y-%m-%d %H:%M:%S")
start=time.mktime(start.timetuple())
end=datetime.datetime.strptime("2021-01-15 10:30:00", "%Y-%m-%d %H:%M:%S")
end=time.mktime(end.timetuple())
print(start)
print(end)

post_at="2021-01-15 02:29 UTC"
target="{note_id}"

while start < end:
	start = round(start + 0.0001, 4)
	print(start)
	random.seed(start)
	user_id=get_random_id()
	random.seed(user_id + post_at)
	note_id = get_random_id()
	if note_id==target:
		print(user_id)
		break
