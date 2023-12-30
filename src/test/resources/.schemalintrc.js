module.exports = {
  connection: {
    host: 'localhost',
    user: 'postgres',
    password: '',
    database: 'mpls-food',
    charset: 'utf8',
  },

  rules: {
    'name-casing': ['error', 'snake'],
    'name-inflection': ['error', 'plural'],
    'prefer-text-to-varchar': ['error'],
    'prefer-timestamptz-to-timestamp': ['error'],
    'prefer-identity-to-serial': ['error'],
    'require-primary-key': ['error'],
  },

  schemas: [{ name: 'public' }],
};
