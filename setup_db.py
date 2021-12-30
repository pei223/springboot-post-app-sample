from typing import Dict, List, Union
import requests
import traceback

test_users = list(
    map(
        lambda i: {
            "name": f"testname{i}",
            "email": f"test{i}@test.com",
            "password": f"test{i}passwd",
        },
        [i for i in range(1, 5)],
    )
)


def generate_posts(name: str) -> List[Dict[str, Union[str, bool]]]:
    return list(
        map(
            lambda i: {
                "title": f"[{name}] title{i}",
                "content": f"[{name}] content: {i}",
                "expose": i % 2 == 0,
            },
            [i for i in range(1, 5)],
        )
    )


base_url = "http://localhost:8888/api"


def setup_users():
    for test_user in test_users:
        res = requests.post(
            f"{base_url}/auth/signup",
            json=test_user,
            headers={"content-type": "application/json"},
        )
        if res.status_code >= 400:
            raise RuntimeError(f"Signup error: {res.status_code}")


def setup_posts():
    for user in test_users:
        res = requests.post(
            f"{base_url}/auth/login/",
            json={"name": user["name"], "password": user["password"]},
            headers={"content-type": "application/json"},
        )
        if res.status_code >= 400:
            raise RuntimeError(f"Signin error: {res.status_code}")
        name, token = res.json()["name"], res.json()["token"]
        print(f"{name}:  {token}")
        for post in generate_posts(name):
            res = requests.post(
                f"{base_url}/posts/",
                json=post,
                headers={
                    "Authorization": f"Bearer {token}",
                    "content-type": "application/json",
                },
            )
            if res.status_code >= 400:
                raise RuntimeError(f"Generate Post error: {res.status_code}")


if __name__ == "__main__":
    try:
        setup_users()
        print("Setup users succeed.")
        setup_posts()
        print("Setup posts succeed.")
    except RuntimeError as e:
        traceback.print_exc()
